package lu.nyo.functionrunner;

import lu.nyo.functionrunner.functions.*;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.of;
import static lu.nyo.functionrunner.FunctionsRunner.create;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionsRunnerTest {
    class TestFunctionFactory extends FunctionFactory<Class<? extends ExecutionUnit>> {
        @Override
        protected <T extends ExecutionUnit> T resolve(Class<? extends ExecutionUnit> tClass) {
            Map<Class, T> inits = new HashMap<>() {{
                put(Test1.class, (T) new Test1());
                put(Test1UnrecognizedPostAction.class, (T) new Test1UnrecognizedPostAction());
                put(Test2.class, (T) new Test2());
                put(Test2Stop.class, (T) new Test2Stop());
                put(Test3.class, (T) new Test3());
                put(Test5.class, (T) new Test5());
                put(Test6.class, (T) new Test6());
                put(Test7.class, (T) new Test7());
                put(Test9.class, (T) new Test9());
                put(Test9Fallback.class, (T) new Test9Fallback());
            }};
            return inits.get(tClass);
        }
    }

    Context context = () -> new TestFunctionFactory();

    private static final Map<String, String> DATA = of("abc", "123");

    FunctionsRunner functionsRunner = create(context);

    @Test
    public void functionRunnerDefaultConstructorAccessTest() {
        Constructor<?> constructor = FunctionsRunner.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), IllegalAccessException.class);
        }
    }

    @Test
    public void test1() {

        Integer a = functionsRunner.runWithResult(DATA,
                0,
                null,
                Test1.class,
                Test2.class);

        assertEquals(a, 0);
    }

    @Test
    public void test2() {

        Integer a = functionsRunner.runWithResult(DATA,
                0,
                null,
                Test1.class,
                Test2Stop.class,
                Test3.class);

        assertEquals(a, 0);
    }

    @Test
    public void test3() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> functionsRunner.runWithResult(DATA,
                0,
                null,
                Test1UnrecognizedPostAction.class,
                Test2.class));
    }


    @Test
    public void test5() {
        FunctionRunnerException functionRunnerException = assertThrows(FunctionRunnerException.class, () -> {
            functionsRunner.runWithResult("test",
                    0,
                    Test9Fallback.class,
                    Test9.class);
        });

        assertEquals(functionRunnerException.getCause().getMessage(), "this is a test fallback");
        assertEquals(functionRunnerException.getContext(), context);
        assertEquals(functionRunnerException.getDataResult(), "the input cannot be reperared");
        assertTrue(functionRunnerException.getNextStepArgs().get("test").equals("test"));
    }

    @Test
    public void test6() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> functionsRunner.runWithResult("test",
                        0,
                        Test10Fallback.class,
                        Test9.class));

        assertTrue(runtimeException.getCause() instanceof ClassNotFoundException);
        assertEquals(runtimeException.getCause().getMessage(), "lu.nyo.functionrunner.FunctionsRunnerTest.TestFunctionFactory Cannot resolve class lu.nyo.functionrunner.functions.Test10Fallback");
    }

    @Test
    public void test7() {
        FunctionRunnerException functionRunnerException = assertThrows(FunctionRunnerException.class, () -> {
            functionsRunner.runWithResult("test",
                    0,
                    Test9Fallback.class,
                    Test8.class);
        });

        assertTrue(functionRunnerException.getExceptionToHandle() instanceof ClassNotFoundException);
        assertEquals(functionRunnerException.getExceptionToHandle().getMessage(), "lu.nyo.functionrunner.FunctionsRunnerTest.TestFunctionFactory Cannot resolve class lu.nyo.functionrunner.functions.Test8");
    }


}
