package demos.gao.aopxml.log;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class BeforeLog implements MethodBeforeAdvice {
    /**
     * @param method
     * @param args
     * @param target
     * @throws Throwable
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("[Class: ]" + target.getClass().getName()
                + "[ Method: ]" + method.getName()
                + "[ Method args: ]" + Arrays.toString(args));
    }
}
