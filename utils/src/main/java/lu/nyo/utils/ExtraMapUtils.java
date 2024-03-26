package lu.nyo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExtraMapUtils {

    private ExtraMapUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Utils Class Not accessible");
    }

    public static <T> Map<String, T> flatten(Map<String, Object> source,
                                             Function<Object, T> transformValues) {

        Map<String, T> result = HashMap.newHashMap(100);
        flatten(source, result, "", transformValues);
        return result;
    }

    private static <T> void flatten(Map<String, Object> source,
                                    Map<String, T> result,
                                    String key,
                                    Function<Object, T> transformValues) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            String nestedKey = key.concat(".").concat(k);
            if (v instanceof Map) {
                flatten((Map<String, Object>) v, result, nestedKey, transformValues);
            } else {
                result.put(nestedKey.substring(1), transformValues.apply(v));
            }
        }
    }
}
