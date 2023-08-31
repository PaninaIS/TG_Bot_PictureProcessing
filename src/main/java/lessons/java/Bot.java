package lessons.java;

import lessons.functions.FilterOperation;
import lessons.utils.ImageUtils;
import lessons.utils.PhotoMessageUtils;
import lessons.utils.RgbMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
       // final  String localFileName="received_image.jpeg";
        Message message=update.getMessage();
        String chatId=message.getChatId().toString();
        try {
            ArrayList<String> photoPaths=new ArrayList<>(PhotoMessageUtils.savePhotos(getFilesByMessage(message),getBotToken()));
            for(String path:photoPaths){
                PhotoMessageUtils.processingImage(path);
                execute(preparePhotoMessage(path,chatId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //SendMessage sendMessage=new SendMessage();
        //sendMessage.setChatId(message.getChatId().toString());
        //sendMessage.setText("Your message"+response);
    }
    private List<org.telegram.telegrambots.meta.api.objects.File> getFilesByMessage(Message message){
        List <PhotoSize> photoSizes=message.getPhoto();// ловим отправленное фото
        ArrayList <org.telegram.telegrambots.meta.api.objects.File> files=new ArrayList<>();
        for (PhotoSize photoSize:photoSizes) {
            final String fileId=photoSize.getFileId();//получаем Id photo которого отправили
            try {
                files.add(sendApiMethod(new GetFile(fileId)));// с помощью метода апи пытаемся сохранить фото
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
     return  files;
    }
    private SendPhoto preparePhotoMessage (String localPath,String chatId){
        SendPhoto sendPhoto=new SendPhoto();
        sendPhoto.setChatId(chatId);// для отправки фото указываем id чата
        InputFile newFile=new InputFile();
        newFile.setMedia(new File(localPath));
        sendPhoto.setPhoto(newFile);// отправляем нашу картинку
        sendPhoto.setCaption("This is Rodriges Hose in red");
        return sendPhoto;
    }


}
