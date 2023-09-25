package lessons.java;

import lessons.commands.AppBotCommand;
import lessons.commands.BotCommonCommands;
import lessons.functions.FilterOperation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    Class[] commandClasses=new Class[]{BotCommonCommands.class};
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
            String response=runCommand(message.getText());
            SendMessage sendMessage=new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(response);
            execute(sendMessage);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
//        try {
//            ArrayList<String> photoPaths=new ArrayList<>(PhotoMessageUtils.savePhotos(getFilesByMessage(message),getBotToken()));
//            for(String path:photoPaths){
//                PhotoMessageUtils.processingImage(path);
//                execute(preparePhotoMessage(path,chatId));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        //SendMessage sendMessage=new SendMessage();
        //sendMessage.setChatId(message.getChatId().toString());
        //sendMessage.setText("Your message"+response);
    }

    private String runCommand(String text) throws InvocationTargetException, IllegalAccessException {
        BotCommonCommands commands=new BotCommonCommands();
        Method[] classMethods=commands.getClass().getDeclaredMethods();
        for (Method method:classMethods ) {
            if(method.isAnnotationPresent(AppBotCommand.class)){
                AppBotCommand command=method.getAnnotation(AppBotCommand.class);
                if(command.name().equals(text)){
                    method.setAccessible(true);
                    return (String) method.invoke(commands);
                }
            }
        }
        return null;
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
        sendPhoto.setReplyMarkup(getKeyboard());
        sendPhoto.setChatId(chatId);// для отправки фото указываем id чата
        InputFile newFile=new InputFile();
        newFile.setMedia(new File(localPath));
        sendPhoto.setPhoto(newFile);// отправляем нашу картинку
        sendPhoto.setCaption("This is Rodriges Hose in red");
        return sendPhoto;
    }

    private ReplyKeyboardMarkup getKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup=new ReplyKeyboardMarkup();//созд новую вирт клавиатуру
        ArrayList <KeyboardRow> allKeyboardRows=new ArrayList<>();
        allKeyboardRows.addAll(getKeyBoardsRows(FilterOperation.class));
        allKeyboardRows.addAll(getKeyBoardsRows(BotCommonCommands.class));

        replyKeyboardMarkup.setKeyboard(allKeyboardRows);//доб все в нашу вир клав-ру
        replyKeyboardMarkup.setOneTimeKeyboard(true);//указываем что отражается клав-ра к одному сообщению а не всегда
        return replyKeyboardMarkup;
    }

    private ArrayList <KeyboardRow> getKeyBoardsRows(Class someClass){
        Method[] classMethods=someClass.getDeclaredMethods();
        ArrayList <AppBotCommand> commands=new ArrayList<>();
        for (Method method:classMethods ) {
            if(method.isAnnotationPresent(AppBotCommand.class)){
              commands.add(method.getAnnotation(AppBotCommand.class));
            }
        }
        ArrayList <KeyboardRow> keyboardRows=new ArrayList<>();
        int columnCount=3;
        int rowsCount=commands.size()/columnCount+(commands.size() % columnCount==0 ? 0:1);
        for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
            KeyboardRow row=new KeyboardRow();
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                int index=rowIndex*columnCount+columnIndex;
                if(index>=commands.size())continue;
                AppBotCommand command=commands.get(rowIndex*columnCount+columnIndex);
                KeyboardButton keyboardButton=new KeyboardButton(command.name());//создаем кнопки с назв методов класса FilterOperation
                row.add(keyboardButton);//кнопки добавляем встроки
            }
            keyboardRows.add(row);//строки сохр в лист строк
        }
        return keyboardRows;
    }

}
