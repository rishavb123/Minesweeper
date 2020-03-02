import java.util.ArrayList;
import java.util.List;

public class Util {
    
    public static abstract class Filter<E> {
        public abstract boolean shouldKeep(E obj);
        
        public List<E> filter(List<E> origList) {
            List<E> list = new ArrayList<>();
            for(E obj: origList)
                if(shouldKeep(obj))
                    list.add(obj);
            return list;
        }
    }
    
    public static <E> List<E> copy(List<E> list) {
        List<E> list2 = new ArrayList<>();
        for(E obj: list) list2.add(obj);
        return list2;
    }

}