package baidu.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Tao on 2018/11/27.
 */

public class MapUtil <T extends Object> {

    public enum IteratorType {
        next,ret
    }

    public interface  IteratorCall<D extends Object> {
        IteratorType  onIterator(Object key ,D value);
    }
    public  static  Object  iteratorMap(Map  map , IteratorCall   iteratorCall) {
        if (map == null)
            return null;
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            Object o = map.get(next);
            switch (iteratorCall.onIterator(next, o)) {
                case ret:
                    return o;
                case next:
                    continue;
            }
        }
        return null;
    }

}
