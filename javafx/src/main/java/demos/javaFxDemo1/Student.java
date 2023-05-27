package demos.javaFxDemo1;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@Data
@NoArgsConstructor
public class Student {
    private String name;
    private int age;
    private String grade;
    private SexEnum sex;

    public Student(String name, int age, String grade, SexEnum sex) {
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.sex = sex;
    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }


    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        pcs.firePropertyChange("name", oldValue, this.name);
    }

    public void setAge(int age) {
        int oldValue = this.age;
        this.age = age;
        pcs.firePropertyChange("age", oldValue, this.age);
    }

}
