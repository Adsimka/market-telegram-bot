package telegram.bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ShippingAddress {
    @Id
    private Long id;

    private String countryCode;

    private String state;

    private String city;

    private String firstStreetLine;

    private String secondStreetLine;

    private String postCode;
}
