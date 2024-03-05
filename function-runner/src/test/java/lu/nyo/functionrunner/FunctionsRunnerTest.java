package lu.nyo.functionrunner;

import lu.nyo.functionrunner.functions.*;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FunctionsRunnerTest {
    class TestFunctionFactory extends FunctionFactory {
        @Override
        protected <T extends ExecutionUnit> T resolve(Class<? extends ExecutionUnit> tClass) {
            Map<Class, T> inits = new HashMap<>() {{
                put(Test1.class, (T) new Test1());
                put(Test1UnrecognizedPostAction.class, (T) new Test1UnrecognizedPostAction());
                put(Test2.class, (T) new Test2());
                put(Test2Stop.class, (T) new Test2Stop());
                put(Test3.class, (T) new Test3());
                put(Test4.class, (T) new Test4());
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

    FunctionsRunner functionsRunner = FunctionsRunner.create(context);

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
        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1.class);
            add(Test2.class);
        }};

        Integer a = functionsRunner.runWithResult(DATA,
                0,
                classLinkedHashMap, null);

        assertEquals(a, 0);
    }

    @Test
    public void test2() {

        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1.class);
            add(Test2Stop.class);
            add(Test3.class);
        }};

        Integer a = functionsRunner.runWithResult(DATA,
                0,
                classLinkedHashMap, null);

        assertEquals(a, 0);
    }

    @Test
    public void test3() {

        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1UnrecognizedPostAction.class);
            add(Test2.class);
        }};

        assertThrowsExactly(UnsupportedOperationException.class, () -> functionsRunner.runWithResult(DATA, 0, classLinkedHashMap, null));
    }

    @Test
    public void test4() {
        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test4.class);
        }};
        assertEquals(functionsRunner.runWithResult(0, 0, classLinkedHashMap, null), 4);
    }

    @Test
    public void test5() {
        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test9.class);
        }};
        FunctionRunnerException functionRunnerException = Assertions.assertThrows(FunctionRunnerException.class, () -> {
            functionsRunner.runWithResult("test", 0, classLinkedHashMap, Test9Fallback.class);
        });

        assertEquals(functionRunnerException.getCause().getMessage(), "this is a test fallback");
        assertEquals(functionRunnerException.getContext(), context);
        assertEquals(functionRunnerException.getDataResult(), "the input cannot be reperared");
        assertTrue(functionRunnerException.getNextStepArgs().get("test").equals("test"));
    }

    @Test
    public void test6() {
        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test9.class);
        }};
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            functionsRunner.runWithResult("test", 0, classLinkedHashMap, Test10Fallback.class);
        });

        assertTrue(runtimeException.getCause() instanceof ClassNotFoundException);

        assertEquals(runtimeException.getCause().getMessage(), "lu.nyo.functionrunner.FunctionsRunnerTest.TestFunctionFactory Cannot resolve lu.nyo.functionrunner.functions.Test10Fallback");
    }

    @Test
    public void test7() {
        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test8.class);
        }};
        FunctionRunnerException functionRunnerException = Assertions.assertThrows(FunctionRunnerException.class, () -> {
            functionsRunner.runWithResult("test", 0, classLinkedHashMap, Test9Fallback.class);
        });

        assertTrue(functionRunnerException.getExceptionToHandle() instanceof ClassNotFoundException);
        assertEquals(functionRunnerException.getExceptionToHandle().getMessage(), "lu.nyo.functionrunner.FunctionsRunnerTest.TestFunctionFactory Cannot resolve lu.nyo.functionrunner.functions.Test8");
    }

}
