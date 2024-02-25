package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.Map;

import static lu.nyo.functionrunner.enums.PostAction.CONTINUE;

public class Test6 implements ExecutionUnit<String> {

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

        executionUnitOutput.setOutput(abc, CONTINUE, Map.of("CLASS", "dasda"));
    }
}
