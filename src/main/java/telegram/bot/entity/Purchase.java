package telegram.bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Purchase {
    @Id
    private String chatId;

    private String currency;
    @OneToMany
    @JoinColumn(name = "id")
    private Laptop laptop;

    private Instant purchaseDate;
    @OneToOne
    private OrderInfo orderInfo;
}
