package peng.gao.aopxml.log;

public class AspectLog {
    public void beforeLog() {
        System.out.println("====方法运行之前运行====");
    }

    public void afterLog() {
        System.out.println("====方法运行之后运行====");
    }
}
