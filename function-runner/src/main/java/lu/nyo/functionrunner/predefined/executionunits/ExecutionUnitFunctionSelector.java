package lu.nyo.functionrunner.predefined.executionunits;

import lu.nyo.functionrunner.ExecutionUnitOutput;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.Map;

import static java.lang.String.format;

public final class ExecutionUnitFunctionSelector implements ExecutionUnit<Object> {
    private static final String ERROR1 = "the CLASS should be defined in the args map and with value of type class that represent an execution unit";

    private static final String ERROR2 = "Class %s cannot be resolved by the function factory %s";

    @Override
    public void execute(Object input,
                        Context context,
                        ExecutionUnitOutput executionUnitOutput,
                        Map<String, Object> args) {
        if (!args.containsKey("CLASS"))
            throw new IllegalArgumentException(ERROR1);
        else if (!(args.get("CLASS") instanceof Class<?>))
            throw new IllegalArgumentException(ERROR1);
        else {
            Class<? extends ExecutionUnit<?>> clazz = (Class<? extends ExecutionUnit<?>>) args.get("CLASS");
            ExecutionUnit executionUnit = context.getFunctionFactory().create(clazz);
            if (executionUnit == null)
                throw new UnsupportedOperationException(format(ERROR2, clazz.getName(), context.getFunctionFactory().getClass().getName()));
            else
                executionUnit.execute(executionUnit.adapt(input, context, args), context, executionUnitOutput, args);
        }

    }
}
