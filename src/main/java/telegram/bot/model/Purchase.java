package telegram.bot.model;

import com.pengrad.telegrambot.model.OrderInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.time.Instant;

@Data
@Builder
public class Purchase {

    private String id;
    private String chatId;
    private String currency;
    private Laptop laptop;
    private Instant purchaseDate;
    private OrderInfo orderInfo;
}
