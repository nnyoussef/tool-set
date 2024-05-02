package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;

public class ExecutionUnit1 implements FunctionExecutionUnit<Integer> {

    @Override
    public Integer adapt(Context context, Object data, ImmutableMap<String, Object> args) {
        return ((Integer) data);
    }

    @Override
    public ExecutionUnitOutput execute(Context context, Integer input, ImmutableMap<String, Object> args) {
        return null;
    }
}
