package demos.gao.annotation;

import org.springframework.stereotype.Repository;
import demos.gao.autowired.IAnimal;

//@Component等价于<bean id="cat" class="simpledemos.gao.autowired.Cat"/>
@Repository
public class Cat implements IAnimal {
    /**
     *
     */
    @Override
    public void voice() {
        System.out.println("Cat Voice");
    }
}
