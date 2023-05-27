package demos.gao.aopxml.log;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AfterLog implements AfterReturningAdvice {
    /**
     * @param returnValue
     * @param method
     * @param args
     * @param target
     * @throws Throwable
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("[Class: ]" + target.getClass().getName()
                + "[ Method Name: ]" + method.getName()
                + "[ Method args: ]" + Arrays.toString(args)
                + "[ return Value: ]" + returnValue);
    }
}
