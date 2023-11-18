package salesAnalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySalesDTO {
    public String category;
    public Float totalSales;
    public Integer count;
}
