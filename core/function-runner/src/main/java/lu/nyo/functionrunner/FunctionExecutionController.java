package lu.nyo.functionrunner;

public interface FunctionExecutionController {
    <T> T apply(Object... arguments);

    String getInstanceId();
}
