package telegram.bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class OrderInfo {
    @Id
    private Long id;

    private String name;

    private String phone_number;

    private String email;

    private ShippingAddress shipping_address;
}
