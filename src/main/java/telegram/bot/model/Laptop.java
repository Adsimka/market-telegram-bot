package telegram.bot.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Laptop {

    private String id;
    private String name;
    private BigDecimal price;
    private String description;
    private String photo;
}
