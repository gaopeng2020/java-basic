package peng.gao.javaConfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Bean1 {
    @Value("yaya")
    private String name;
    @Value("20")
    private int age;
}
