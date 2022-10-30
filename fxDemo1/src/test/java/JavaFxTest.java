import javafx.application.Application;
import org.junit.jupiter.api.Test;
import peng.gao.fxdemo1.HelloApplication;
import peng.gao.fxdemo1.SexEnum;
import peng.gao.fxdemo1.Student;

public class JavaFxTest {
    @Test
    void helloApplicationTest() {
        String[] args = new String[]{};
        Application.launch(HelloApplication.class, args);
    }

    @Test
    void propertyChangeNotifyTest() {
        Student student1 = Student.builder().name("AAA").age(20).grade("八年级").sex(SexEnum.Boy).build();
        student1.addPropertyChangeListener(evt -> {
            System.out.println("evt.getOldValue() = " + evt.getOldValue());
            System.out.println("evt.getNewValue() = " + evt.getNewValue());
        });
        student1.setAge(18);

        student1.getPcs().addPropertyChangeListener("name", evt -> {
            System.out.println("evt.getOldValue() = " + evt.getOldValue());
            System.out.println("evt.getNewValue() = " + evt.getNewValue());
        });
        student1.setName("BBB");
    }

}
