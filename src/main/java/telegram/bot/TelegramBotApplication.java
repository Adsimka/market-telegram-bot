package telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PreCheckoutQuery;
import com.pengrad.telegrambot.model.SuccessfulPayment;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.request.CreateInvoiceLink;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.StringResponse;
import lombok.Builder;
import telegram.bot.service.LaptopService;
import telegram.bot.service.OrderService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelegramBotApplication extends TelegramBot {

    private final ExecutorService executorService;
    private final LaptopService laptopService;
    private final OrderService orderService;
    private final String providerToken;

    private final InlineKeyboardMarkup keyboardMarkup;

    private static final String SUPPORT_MARKET_NAME = "kelly_oubre";
    private static final String TELEGRAM_DOMAIN = "t.me/%s";
    private static final String SUPPORT_MARKET_LINK = String.format(TELEGRAM_DOMAIN, SUPPORT_MARKET_NAME);


    @lombok.Builder
    public TelegramBotApplication(String botToken, String providerToken) {
        super(botToken);
        this.providerToken = Optional.ofNullable(providerToken).orElse("");
        this.executorService = Executors.newFixedThreadPool(8);
        this.laptopService = LaptopService.getInstance();
        this.orderService = OrderService.getInstance();

        keyboardMarkup = new InlineKeyboardMarkup(new InlineKeyboardButton("Поддержка")
                .url(SUPPORT_MARKET_LINK));
    }

    public void run() {
        this.setUpdatesListener(updates -> {
            updates.stream()
                    .<Runnable>map(update -> () -> process(update))
                    .forEach(executorService::submit);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, exception -> System.out.println(exception.response().description()));
    }

    private void process(Update update) {
        Message message = update.message();
        if (message != null) {
            Optional.ofNullable(message.text())
                    .ifPresent(commandName -> this.serveCommand(commandName, message.chat().id()));
            Optional.ofNullable(message.successfulPayment())
                    .ifPresent(payment -> servePayment(payment, message.chat().id()));
        } else if (update.preCheckoutQuery() != null) {
            PreCheckoutQuery preCheckoutQuery = update.preCheckoutQuery();
            execute(new AnswerPreCheckoutQuery(preCheckoutQuery.id()));
        }
    }

    private void servePayment(SuccessfulPayment payment, Long id) {
        orderService.registryPurchase(payment, id);
    }

    private void serveCommand(String commandName, Long chatId) {
        switch (commandName) {
            case "/start": {
                SendMessage response = new SendMessage(chatId,
                        "Добро пожаловать в Market!\n\nСписок команд:\n  /menu - Главное меню\n  /start - Начало работы");
                this.execute(response);
                break;
            }
            case "/menu": {
                SendMessage response = new SendMessage(chatId, "Выберите раздел:")
                        .replyMarkup(new ReplyKeyboardMarkup(new String[][] {
                                {"Товары", "Отзывы"},
                                {"Поддержка"}
                        }).resizeKeyboard(true));
                this.execute(response);
                break;
            }
            case "Товары": {
                laptopService.getLaptops()
                        .forEach(laptop -> {
                            CreateInvoiceLink link = new CreateInvoiceLink(laptop.getName(), laptop.getDescription(), laptop.getId(),
                                    providerToken, "RUB",
                                    new LabeledPrice("Цена", laptop.getPrice().multiply(BigDecimal.valueOf(100L)).intValue()))
                                    .needShippingAddress(true)
                                    .photoUrl(laptop.getPhoto())
                                    .needName(true)
                                    .needPhoneNumber(true);
                            StringResponse response = execute(link);
                            SendPhoto sendPhoto = new SendPhoto(chatId, laptop.getPhoto())
                                    .caption(String.format("%s - %s", laptop.getName(), laptop.getDescription()))
                                    .replyMarkup(new InlineKeyboardMarkup(
                                            new InlineKeyboardButton("Оплатить").url(response.result())
                                    ));
                            execute(sendPhoto);
                        });
                break;
            }
            case "Отзывы": {
                sendMessage(chatId, "На данный момент Market не содержит отзывов!");
                break;
            }
            case "Поддержка": {
                SendMessage sendMessage = new SendMessage(chatId, "Есть вопросы?")
                        .replyMarkup(keyboardMarkup);
                this.execute(sendMessage);
                break;
            }
            default: {
                sendMessage(chatId, "Команда не распознана!");
                break;
            }
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage response = new SendMessage(chatId, message);
        this.execute(response);
    }

}
