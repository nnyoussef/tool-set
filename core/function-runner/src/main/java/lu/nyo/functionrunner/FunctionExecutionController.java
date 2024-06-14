package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;

public interface FunctionExecutionController {

    <T> T apply(Object... arguments);

    default <T> T apply(Context context,
                        FunctionIterator functionIterator,
                        ImmutableMap<String, Object> args) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    default String getInstanceId() {
        return getClass().getName();
    }

}
