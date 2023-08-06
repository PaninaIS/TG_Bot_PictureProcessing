package lessons.java;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "pictureProccesingBot";
    }
    @Override
    public  String getBotToken(){
        return "6512496752:AAE1czvyiYIspCyzyYmD3LjEPEj6wWAMKyk";
    }
    @Override
    public void onUpdateReceived(Update update) {
        Message message=update.getMessage();
        System.out.println(message.getText());
    }


}
