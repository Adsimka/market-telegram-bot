package telegram.bot.model;

import com.pengrad.telegrambot.model.OrderInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Laptop laptop;

    private Instant purchaseDate;

    private OrderInfo orderInfo;

}
