package salesAnalysis.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {
    public Integer orderItemId;
    public Integer orderId;
    public Integer productId;
    public Integer quantity;
    public Float pricePerUnit;
}
