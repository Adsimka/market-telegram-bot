package telegram.bot.entity;

import jakarta.persistence.Column;
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
    @Column(nullable = false)
    private String countryCode;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String firstStreetLine;

    private String secondStreetLine;
    @Column(nullable = false)
    private String postCode;
}
