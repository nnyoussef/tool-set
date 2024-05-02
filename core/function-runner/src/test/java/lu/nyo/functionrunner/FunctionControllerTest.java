package lu.nyo.functionrunner;


import org.junit.jupiter.api.Test;

public class FunctionControllerTest {
    FunctionManager functionManager = new FunctionManager() {
        @Override
        protected void handleReportedOutput(Context context, ExecutionUnitOutput executionUnitOutput) {

        }

        @Override
        protected void handleReportedException(Context context, ExecutionUnitOutput executionUnitOutput, Exception exception) {

        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public FunctionExecutionUnit<Object> next() {
            return null;
        }

    };

    @Test
    public void test1() {
    }
}
