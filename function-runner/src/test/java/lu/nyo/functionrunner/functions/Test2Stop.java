package lu.nyo.functionrunner.functions;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import static lu.nyo.functionrunner.enums.PostAction.STOP;

public class Test2Stop implements ExecutionUnit<Integer> {

    @Override
    public void execute(Integer input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        ImmutableMap<String, Object> args) {
        executionUnitOutput.setOutput(input * 0, STOP, null);
    }

}
