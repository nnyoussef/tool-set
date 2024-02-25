package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.enums.PostAction;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.Map;

public class Test4 implements ExecutionUnit<Integer> {

    @Override
    public void execute(Integer input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        Map<String, Object> args) {
        if (input < 4) {
            input += 1;
            executionUnitOutput.setOutput(input, PostAction.REPEAT, null);
        } else
            executionUnitOutput.setOutput(input, PostAction.STOP, null);
    }
}
