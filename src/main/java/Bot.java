import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    //psvm
    public static void main(String[] args) {
        System.out.println("Bot is running...");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sm = new SendMessage();
        sm.enableMarkdown(true);
        sm.setChatId(message.getChatId().toString());
        sm.setReplyToMessageId(message.getMessageId());
        sm.setText(text);
        try {
            sendButtons(sm);
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String text = message.getText();
            if (("/help").equals(text)) {
                sendMsg(message, "Чем могу помочь?");
            } else if (("/settings").equals(text)) {
                sendMsg(message, "Что будем настраивать?");
            } else {
                try {
                    sendMsg(message, Weather.getWeather(text, model));
                } catch (IOException e) {
                    sendMsg(message, "Город не найден");
                }
            }
        }
    }

    public String getBotUsername() {
        return "MyDRWPBot";
    }

    public String getBotToken() {
        return "634724406:AAHvKTEhZ7GbNNiTmsJYQFHSKf8M1loCuSw";
    }
}
