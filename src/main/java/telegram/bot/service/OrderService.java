package telegram.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.SuccessfulPayment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import telegram.bot.config.ObjectMapperConfig;
import telegram.bot.entity.Laptop;
import telegram.bot.entity.OrderInfo;
import telegram.bot.entity.Purchase;
import telegram.bot.entity.ShippingAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderService {

    private final LaptopService laptopService;

    private final ObjectMapper objectMapper;

    private static final OrderService INSTANCE = new OrderService(LaptopService.getInstance(),
            ObjectMapperConfig.getInstance());

    public Optional<Purchase> registryPurchase(SuccessfulPayment payment, Long chatId) {
        String laptopId = payment.invoicePayload();

        Optional<Purchase> purchase = laptopService.getLaptops().stream()
                .filter(laptop -> laptop.getId().equals(laptopId))
                .findAny()
                .map(laptop -> buildPurchase(payment, chatId, laptop));

        return purchase;
    }

    private static Purchase buildPurchase(SuccessfulPayment payment, Long chatId, Laptop laptop) {
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
                .laptop(laptop)
                .purchaseDate(Instant.now())
                .build();
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

}
