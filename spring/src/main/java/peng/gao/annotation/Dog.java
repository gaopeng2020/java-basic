package peng.gao.annotation;

import org.springframework.stereotype.Repository;
import peng.gao.autowired.IAnimal;

@Repository
public class Dog implements IAnimal {
    /**
     *
     */
    @Override
    public void voice() {
        System.out.println("Dog Voice");
    }
}
