package peng.gao.javaConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@Configuration代表一个配置类，和beans.xml的作用一样
@Configuration
@ComponentScan("peng.gao.javaConfig")
public class AppConfig {
    @Bean
    //等价于beans.xml中的一个bean标签，方法名称相当于id; 返回值相当于 class
    public Bean1 bean1() {
        return new Bean1();
    }

}
