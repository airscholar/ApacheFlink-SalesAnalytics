package salesAnalysis.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    public Integer productId;
    public String name;
    public String description;
    public String category;
    public Float price;
}
