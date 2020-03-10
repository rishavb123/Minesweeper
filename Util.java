import java.util.ArrayList;
import java.util.List;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.File;
import java.awt.GraphicsEnvironment;

public class Util {

    private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    public static abstract class Filter<E> {
        public abstract boolean shouldKeep(E obj);

        public List<E> filter(List<E> origList) {
            List<E> list = new ArrayList<>();
            for (E obj : origList)
                if (shouldKeep(obj))
                    list.add(obj);
            return list;
        }
    }

    public static <E> List<E> copy(List<E> list) {
        List<E> list2 = new ArrayList<>();
        for (E obj : list)
            list2.add(obj);
        return list2;
    }

    public static Font createFont(String path) {
        try {
            Font f =  Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(25f);
            ge.registerFont(f);
            return f;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}