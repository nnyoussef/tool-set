package lu.nyo.functionrunner.functions;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.Map;

@SuppressWarnings("ALL")
public class Test3 implements ExecutionUnit<Integer> {

    @Override
    public void execute(Integer input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        ImmutableMap<String, Object> args) {
        String a = null;
        // null on purpose
        a.length();
    }

}
