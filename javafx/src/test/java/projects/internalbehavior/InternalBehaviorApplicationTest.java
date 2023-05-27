package projects.internalbehavior;

import javafx.application.Application;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Arrays;

public class InternalBehaviorApplicationTest {
    @Test
    public void InternalBehaviorAppLaunch1() {
        Application.launch(InternalBehaviorApplication.class);
    }

    @Test
    public void InternalBehaviorAppLaunch2() {
        InternalBehaviorApplication internalBehaviorApp = new InternalBehaviorApplication();
        try {
            internalBehaviorApp.launchAppBySwing();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void InternalBehaviorAppLaunch3() {
        Thread currentThread = Thread.currentThread();
        SwingUtilities.invokeLater(() -> new InternalBehaviorApp(currentThread));

        synchronized (currentThread) {
            try {
                currentThread.wait();
            } catch (InterruptedException e) {
                System.out.println("e.getStackTrace() = " + Arrays.toString(e.getStackTrace()));
            }
        }
        System.out.println("========End+=========");
    }
}
