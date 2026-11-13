package tetris.utils;

import java.util.HashSet;
import java.util.Set;

public class SimpleMath {
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }
}
