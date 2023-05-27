package demos.gao.annotation;

import org.springframework.stereotype.Repository;
import demos.gao.autowired.IAnimal;

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
