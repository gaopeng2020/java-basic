package demos.gao.javaConfig;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class javaConfigTest {
    @Test
    public void test1(){
        ApplicationContext context = new AnnotationConfigApplicationContext(Bean1.class);
        Bean1 bean1 = context.getBean("bean1", Bean1.class);
        System.out.println("bean1.toString() = " + bean1.toString());

    }
}
