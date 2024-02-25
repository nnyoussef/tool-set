package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.Map;

import static lu.nyo.functionrunner.enums.PostAction.CONTINUE;

public class Test2 implements ExecutionUnit<Integer> {

    @Override
    public void execute(Integer input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        Map<String, Object> args) {
        executionUnitOutput.setOutput(input * 0, CONTINUE, null);
    }

}
