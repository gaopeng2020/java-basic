package peng.gao.autowired;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutoWiredTest {
    @Test
    public void test1(){
        String xmlPath = "beans.xml";
        ApplicationContext xmlAppContext = new ClassPathXmlApplicationContext(xmlPath);
        People person = xmlAppContext.getBean("people", People.class);
        System.out.println("person = " + person);
        person.getCat().voice();
        person.getDog().voice();
    }
}
