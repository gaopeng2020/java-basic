package peng.gao.aopAspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect //标记这个类是个切面
public class AopAnnotation {

    @Before("execution(* peng.gao.aopxml.service.UserServiceImpl.*(..))")
    public void beforeLog() {
        System.out.println("====方法运行之前运行====");
    }

    @After("execution(* peng.gao.aopxml.service.UserServiceImpl.*(..))")
    public void afterLog() {
        System.out.println("====方法运行之后运行====");
    }

    @Around("execution(* peng.gao.aopxml.service.UserServiceImpl.*(..))")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("====Before Around===="+pjp.getSignature());

        //方法执行中
        pjp.proceed();

        //环绕后
        System.out.println("====After Around===="+pjp.getSignature());
    }
}
