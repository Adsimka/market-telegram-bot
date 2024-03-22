package telegram.bot;

public class DemoApp {

    private static final String BOT_TOKEN = "7123068826:AAHFqBfgiDGZy5tk_SrWLKfrc7UgIThkamk";

    private static final String PROVIDER_TOKEN = "390540012:LIVE:47719";

    public static void main(String[] args) {
        TelegramBotApplication application = TelegramBotApplication.builder()
                .botToken(BOT_TOKEN)
                .providerToken(PROVIDER_TOKEN)
                .build();
        application.run();
    }

}