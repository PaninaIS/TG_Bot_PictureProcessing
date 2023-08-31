package lessons.utils;

import lessons.functions.ImageOperation;

import java.awt.image.BufferedImage;

public class RgbMaster {
    private BufferedImage image;
    private  int width;
    private int height;
    private boolean hasAlphaChannel;
    private  int[] pixels;

    public BufferedImage getImage() {
        return image;
    }

    public RgbMaster(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getWidth();
        this.hasAlphaChannel = image.getAlphaRaster()!=null;
        this.pixels = image.getRGB(0,0,width,height,null,0,width);
    }

    public void changeImage (ImageOperation operation) throws Exception {
        for(int i=0;i <pixels.length;i++){
            float[]pixel=ImageUtils.rgbIntToArray(pixels[i]);
            float[]newPixel= operation.execute(pixel);
            pixels[i]=ImageUtils.arrayToRgbInt(newPixel);
        }
        image.setRGB(0,0,width,height,pixels,0,width);
    }
}
