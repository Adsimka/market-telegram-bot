package telegram.bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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
