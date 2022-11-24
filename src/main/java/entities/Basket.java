package entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Basket {
    private Long productId;
    private Long userId;
    private Integer count;

}
