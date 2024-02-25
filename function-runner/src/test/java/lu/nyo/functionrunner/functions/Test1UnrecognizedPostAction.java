package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.Map;

public class Test1UnrecognizedPostAction implements ExecutionUnit<String> {

    @Override
    public String adapt(Object data,
                        Context context,
                        Map<String, Object> args) {
        return data.toString();
    }

    @Override
    public void execute(String input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        Map<String, Object> args) {
        Integer abc = input.length() * 10;
        executionUnitOutput.setOutput(abc, null, null);
    }
}
