package lessons.java;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

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
        String response=message.getFrom().getId().toString();
        PhotoSize photoSize=message.getPhoto().get(0);// ловим отправленное фото
        final String fileId=photoSize.getFileId();//получаем Id photo которого отправили
        try {
            final org.telegram.telegrambots.meta.api.objects.File file=sendApiMethod(new GetFile(fileId));// с помощью метода апи пытаемся сохранить фото
            final String imageUrl="https://api.telegram.org/file/bot"+getBotToken()+"/"+file.getFilePath();// на этом url храняться фото в телеграмм
            saveImage(imageUrl,"received_image.jpeg");
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(message.getText());
        SendPhoto sendPhoto=new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());// для отправки фото указываем id чата
        InputFile newFile=new InputFile();
        newFile.setMedia(new File("src/image.jpeg"));
        sendPhoto.setPhoto(newFile);// отправляем нашу картинку
        sendPhoto.setCaption("This is Rodriges Hose");



        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Your message"+response);
        try {
            execute(sendMessage);
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(String url, String fileName) throws IOException {
        URL urlModel=new URL(url);
        InputStream inputStream=urlModel.openStream();
        OutputStream outputStream=new FileOutputStream(fileName);
        byte[]b=new byte[2048];//скачиваем картинку по кусочкам байт
        int length;
        while((length=inputStream.read(b))!=-1){
            outputStream.write(b,0,length);
        }
        inputStream.close();
        outputStream.close();
    }


}
