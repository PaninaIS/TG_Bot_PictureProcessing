package lessons.functions;

import java.lang.reflect.InvocationTargetException;

public interface ImageOperation {
    float[] execute(float[] rgb) throws InvocationTargetException, IllegalAccessException;//будем изменять изображение с помощью лямбд
}
