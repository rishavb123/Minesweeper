import java.util.List;

public class Util {
    
    public abstract class Filter<E> {
        public abstract boolean shouldKeep(E obj);
        
        public List<E> filter(List<E> list) {
            for(E obj: list)
                if(!shouldKeep(obj))
                    list.remove(obj);
            return list;
        }
    }
        
}