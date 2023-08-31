package lessons.utils;

import lessons.functions.FilterOperation;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PhotoMessageUtils {
    public static List<String> savePhotos(List<File> files,String botToken) throws IOException {
        Random random=new Random();
        ArrayList <String> paths=new ArrayList<>();
        for (File file:files ) {
            // PhotoSize photoSize=message.getPhoto().get(0);// ловим отправленное фото
            final String imageUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();// на этом url храняться фото в телеграмм
            final String localFileName = "C:\\Users\\user\\IdeaProjects\\TG_Bot_PictureProcessing\\images/" + new Date().getTime() + random.nextLong() + ".jpeg";
            saveImage(imageUrl, localFileName);
            paths.add(localFileName);
        }
        return paths;
    }

    public static void saveImage(String url, String fileName) throws IOException {
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

    public static void processingImage(String fileName) throws Exception {
        final BufferedImage image= ImageUtils.getImage(fileName);
        final RgbMaster rgbMaster=new RgbMaster(image);
        rgbMaster.changeImage(FilterOperation::onlyRed);
        ImageUtils.saveImage(rgbMaster.getImage(),fileName);
    }
        //String response=message.getFrom().getId().toString(); текстовое сообщение

}


