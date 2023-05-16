package peng.gao.autowired;

//@Component等价于<bean id="cat" class="peng.gao.autowired.Cat"/>
public class Cat implements IAnimal{
    /**
     *
     */
    @Override
    public void voice() {
        System.out.println("Cat Voice");
    }
}
