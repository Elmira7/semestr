package entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Promotion {

    private Long id;
    private Date dateBegin;
    private Date dateEnd;
    private String name;
    private String description;
    private Integer discount;

}
