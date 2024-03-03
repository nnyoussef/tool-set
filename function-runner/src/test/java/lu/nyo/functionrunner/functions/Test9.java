package lu.nyo.functionrunner.functions;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;


public class Test9 implements ExecutionUnit<String> {


    @Override
    public void execute(String input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        ImmutableMap<String, Object> args) {
        executionUnitOutput.setOutput("the input cannot be reperared", null, ImmutableMap.of("test", "test"));
        throw new RuntimeException();
    }

}
