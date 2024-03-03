package lu.nyo.functionrunner.functions;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

public class Test1UnrecognizedPostAction implements ExecutionUnit<String> {

    @Override
    public String adapt(Object data,
                        Context context,
                        ImmutableMap<String, Object> args) {
        return data.toString();
    }

    @Override
    public void execute(String input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        ImmutableMap<String, Object> args) {
        Integer abc = input.length() * 10;
        executionUnitOutput.setOutput(abc, null, null);
    }
}
