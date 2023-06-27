package demos.gao.annotation;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 * 注解 @Component 等价于在IOC容器中的<bean id="cat" class="simpledemos.gao.annotation.People"/>
 * 三个子注解：@Repository（DAO）  @Service  @Controller ，作用一样，向IOC容器中注册一个对象
 */
@Data
@Controller
public class People {
    /*
     * Autowired注解可以通过名称的方式实现自动装配的作用；
     * 如果Autowired的required=false，则该属性可以为null
     * 当IOC容器中有多个对象，后者对象的名称不是标准的名称，可以配合Qualifier来指定名称
     * @Resource的功能相当于Autowired+Qualifier(测试不可用)
     * */
    @Autowired(required = false)
    @Qualifier(value = "dog")
    private Dog dog;
    @Autowired
    private Cat cat;

    //    @Value() 等价于<property name="name" value="yaya"/>
    @Value("yaya")
    private String name;
}
