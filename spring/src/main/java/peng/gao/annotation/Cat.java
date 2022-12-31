package peng.gao.annotation;

import org.springframework.stereotype.Repository;
import peng.gao.autowired.IAnimal;

//@Component等价于<bean id="cat" class="peng.gao.autowired.Cat"/>
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
