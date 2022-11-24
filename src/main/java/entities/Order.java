package entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class Order {
    private Long id;
    private Long idUser;
    private Date date;
    private String status;
    private Integer number;
    private List<Entity> orderEntity;

    @Data
    @Builder
    public static class Entity {
        private Long idProduct;
        private Integer count;
        private Integer price;
    }
}
