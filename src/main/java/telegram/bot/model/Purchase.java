package telegram.bot.model;

import com.pengrad.telegrambot.model.OrderInfo;
import lombok.Builder;
import lombok.Data;

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
