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
import telegram.bot.entity.Product;
import telegram.bot.service.OrderService;
import telegram.bot.service.ProductService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static telegram.bot.util.MessageConstant.*;

public class TelegramBotApplication extends TelegramBot {

    private final ExecutorService executorService;
    private final ProductService productService;
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
        this.productService = ProductService.getInstance();
        this.orderService = OrderService.getInstance();

        keyboardMarkup = new InlineKeyboardMarkup(new InlineKeyboardButton(TECHNICAL_SUPPORT)
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

    private void serveCommand(String commandName, Long chatId) {
        switch (commandName) {
            case START_COMMAND: {
                SendMessage response = new SendMessage(chatId, WELCOME_MESSAGE);
                this.execute(response);
                break;
            }
            case MENU_COMMAND: {
                SendMessage response = new SendMessage(chatId, SELECT_SECTION)
                        .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                                {PURCHASE, FEEDBACK},
                                {TECHNICAL_SUPPORT}
                        }).resizeKeyboard(true));
                this.execute(response);
                break;
            }
            case PURCHASE: {
                productService.getLaptops()
                        .forEach(laptop -> {
                            sendProduct(chatId, laptop);
                        });
                break;
            }
            case FEEDBACK: {
                sendMessage(chatId, NOT_FEEDBACK);
                break;
            }
            case TECHNICAL_SUPPORT: {
                SendMessage sendMessage = new SendMessage(chatId, CHANGE_CHANNEL)
                        .replyMarkup(keyboardMarkup);
                this.execute(sendMessage);
                break;
            }
            default: {
                sendMessage(chatId, COMMAND_NOT_RECOGNIZED);
                break;
            }
        }
    }

    private void sendProduct(Long chatId, Product laptop) {
        CreateInvoiceLink link = new CreateInvoiceLink(laptop.getName(), laptop.getDescription(), laptop.getId(),
                providerToken, RUB,
                new LabeledPrice(PRICE, laptop.getPrice().multiply(BigDecimal.valueOf(100L)).intValue()))
                .needShippingAddress(true)
                .photoUrl(laptop.getPhoto())
                .needName(true)
                .needPhoneNumber(true);
        StringResponse response = execute(link);
        SendPhoto sendPhoto = new SendPhoto(chatId, laptop.getPhoto())
                .caption(String.format("%s - %s", laptop.getName(), laptop.getDescription()))
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton(PAY).url(response.result())
                ));
        execute(sendPhoto);
    }

    private void servePayment(SuccessfulPayment payment, Long chatId) {
        orderService.registryOrder(payment, chatId);
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage response = new SendMessage(chatId, message);
        this.execute(response);
    }

}
