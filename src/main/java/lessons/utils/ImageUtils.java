package lessons.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    static public BufferedImage getImage(String path) throws IOException {
        final File file=new File(path);
        return ImageIO.read(file);
    }
    static public void saveImage(BufferedImage image,String path) throws IOException {
       ImageIO.write(image,"png",new File(path)) ;
    }

    static float [] rgbIntToArray(int pixel){     //из полученны нами int-пикселей картинки мы переводим в цвета
    Color color=new Color(pixel);
    return color.getRGBColorComponents(null);
    }
    static int arrayToRgbInt(float[] pixel) throws Exception {  //наоборот из массива цветов
        Color color = null;
        if(pixel.length==3){
            color=new Color(pixel[0],pixel[1],pixel[2]);
        }else if(pixel.length==4){
            color=new Color(pixel[0],pixel[1],pixel[2],pixel[3]);
        }
        if(color !=null) {
            return color.getRGB();
        }
        throw new Exception ("invalide color");
    }

}
