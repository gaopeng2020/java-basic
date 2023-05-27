package demos.gao.aopxml;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import demos.gao.aopxml.service.IUserService;

public class AopTest {
    @Test
   public void test1(){
        String xmlPath = "ApplicaationContext.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);

        //AOP使用的是动态代理，而动态代理代理的是接口，因此context.getBean的对象类型 必须是个接口类型
        IUserService userService = context.getBean("userService", IUserService.class);
        userService.add();
    }
}
