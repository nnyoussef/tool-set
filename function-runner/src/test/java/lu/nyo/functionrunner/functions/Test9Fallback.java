package lu.nyo.functionrunner.functions;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.FunctionRunnerException;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;


public class Test9Fallback implements ExecutionUnit<FunctionRunnerException> {


    @Override
    public void execute(FunctionRunnerException input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        ImmutableMap<String, Object> args) {
        input.initCause(new Throwable("this is a test fallback"));
    }

}
