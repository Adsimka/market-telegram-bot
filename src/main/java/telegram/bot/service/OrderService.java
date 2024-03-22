package telegram.bot.service;

import com.pengrad.telegrambot.model.SuccessfulPayment;
import lombok.RequiredArgsConstructor;
import telegram.bot.entity.Product;
import telegram.bot.entity.OrderInfo;
import telegram.bot.entity.Purchase;
import telegram.bot.entity.ShippingAddress;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;

    private static final OrderService INSTANCE = new OrderService(
            ProductService.getInstance()
    );

    public Optional<Purchase> registryPurchase(SuccessfulPayment payment, Long chatId) {
        String laptopId = payment.invoicePayload();

        Optional<Purchase> purchase = productService.getLaptops().stream()
                .filter(product -> product.getId().equals(laptopId))
                .findAny()
                .map(product -> buildPurchase(payment, chatId, product));

        return purchase;
    }

    private static Purchase buildPurchase(SuccessfulPayment payment, Long chatId, Product product) {
        return Purchase.builder()
                .currency(payment.currency())
                .chatId(chatId.toString())
                .orderInfo(OrderInfo.builder()
                        .name(payment.orderInfo().name())
                        .email(payment.orderInfo().email())
                        .phoneNumber(payment.orderInfo().phoneNumber())
                        .shippingAddress(ShippingAddress.builder()
                                .countryCode(payment.orderInfo()
                                        .shippingAddress()
                                        .countryCode())
                                .state(payment.orderInfo()
                                        .shippingAddress()
                                        .state())
                                .city(payment.orderInfo()
                                        .shippingAddress()
                                        .city())
                                .firstStreetLine(payment.orderInfo()
                                        .shippingAddress()
                                        .streetLine1())
                                .secondStreetLine(payment.orderInfo()
                                        .shippingAddress()
                                        .streetLine2())
                                .postCode(payment.orderInfo()
                                        .shippingAddress()
                                        .postCode())
                                .build())
                        .build())
                .product(product)
                .purchaseDate(Instant.now())
                .build();
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

}
