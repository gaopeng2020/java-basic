package ept.gut.javaFxDemo1;

import ept.gui.javaFxDemo1.HelloApplication;
import javafx.application.Application;
import org.junit.jupiter.api.Test;

public class HelloApplicationTest {
    @Test
    public void helloAppLaunch(){
        Application.launch(HelloApplication.class);
    }
}
