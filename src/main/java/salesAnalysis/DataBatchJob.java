package salesAnalysis;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.configuration.Configuration;
import salesAnalysis.dto.CategorySalesDTO;
import salesAnalysis.entities.OrderItem;
import salesAnalysis.entities.Product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class DataBatchJob {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<OrderItem> orderItems = env
                .readCsvFile("~/mylocalsystem/Datasets/order_items.csv")
                .ignoreFirstLine()
                .pojoType(OrderItem.class, "orderItemId", "orderId", "productId", "quantity", "pricePerUnit");

        DataSource<Product> products = env
                .readCsvFile("~/mylocalsystem/Datasets/products.csv")
                .ignoreFirstLine()
                .pojoType(Product.class, "productId", "name", "description", "price", "category");

        //join the datasets on the product Id
        DataSet<Tuple6<String, String, Float, Integer, Float, String>> joined = orderItems
                .join(products)
                .where("productId")
                .equalTo("productId")
                .with((JoinFunction<OrderItem, Product, Tuple6<String, String, Float, Integer, Float, String>>) (first, second)
                        -> new Tuple6<>(
                        second.productId.toString(),
                        second.name,
                        first.pricePerUnit,
                        first.quantity,
                        first.pricePerUnit * first.quantity,
                        second.category
                ))
                .returns(TypeInformation.of(new TypeHint<Tuple6<String, String, Float, Integer, Float, String>>() {
                }));

        // group by category to get the total sales and count
        DataSet<CategorySalesDTO> categorySales = joined
                .map((MapFunction<Tuple6<String, String, Float, Integer, Float, String>, CategorySalesDTO>) record
                        -> new CategorySalesDTO(record.f5, record.f4, 1))
                .returns(CategorySalesDTO.class)
                .groupBy("category")
                .reduce((ReduceFunction<CategorySalesDTO>) (value1, value2) ->
                        new CategorySalesDTO(value1.getCategory(), value1.getTotalSales() + value2.getTotalSales(),
                                value1.getCount() + value2.getCount()));

        //sort by total sales in descending order
        categorySales.sortPartition("totalSales", Order.DESCENDING).print();

//        //convert to tuple
//        DataSet<Tuple3<String, Float, Integer>> conversion = categorySales
//                .map((MapFunction<CategorySalesDTO, Tuple3<String, Float, Integer>>) record
//                        -> new Tuple3<>(record.getCategory(), record.getTotalSales(), record.getCount()))
//                .returns(new TypeHint<Tuple3<String, Float, Integer>>() {
//                });
//
//        conversion.writeAsCsv("/Users/airscholar/Dev/Projects/Flink/SalesAnalysis/output/tuple-output.csv",
//                "\n", ",", FileSystem.WriteMode.OVERWRITE);

        categorySales.output(new OutputFormat<CategorySalesDTO>() {
            private transient BufferedWriter writer;

            @Override
            public void configure(Configuration configuration) {
                // configuration steps (if needed) can be here
            }

            @Override
            public void open(int taskNumber, int numTasks) throws IOException {
                File outputFile = new File("~/mylocalsystem/output/new-output.csv");
                this.writer = new BufferedWriter(new FileWriter(outputFile, true));
            }

            @Override
            public void writeRecord(CategorySalesDTO categorySalesDTO) throws IOException {
                writer.write(categorySalesDTO.getCategory()
                        + "," + categorySalesDTO.getTotalSales()
                        + "," + categorySalesDTO.getCount());
                writer.newLine();
            }

            @Override
            public void close() throws IOException {
                writer.close();
            }
        });

        // Execute program, beginning computation.
        env.execute("Sales Analysis");
    }
}
