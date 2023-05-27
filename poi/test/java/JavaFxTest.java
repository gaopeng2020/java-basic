import javafx.application.Application;
import org.junit.Test;
import demos.gao.fxdemo1.HelloApplication;

public class JavaFxTest {
    @Test
    public void fxTest() {
        String[] args = new String[]{};
        Application.launch(HelloApplication.class, args);
    }
}
