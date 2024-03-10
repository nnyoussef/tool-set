package lu.nyo.sbjsconfig;

import java.util.function.Function;

public interface Plugin<T, R> extends Function<T, R> {
    String getName();
}
