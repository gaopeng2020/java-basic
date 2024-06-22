package log4j;


import lombok.extern.slf4j.Slf4j;

/**
 * @author gaopeng
 */

@Slf4j
public class Log4jTest {
//    private static final Logger log = LoggerFactory.getLogger(Log4jTest.class); //可以直接使用@Slf4j代替

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            log.debug("调试debug信息");
            log.info("普通Info信息");
            log.warn("警告warn信息");
            log.error("error信息");

            // 将系统的异常信息输出
            try {
                int j = 1 / 0;
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("出现异常：", e);
            }
            System.out.println("System.out.println");
            log.trace("控制台打印彩色日志:点击右上角->Edit Configurations，在VM options中添加 -Dlog4j.skipJansi=false\n");
        }
    }
}
