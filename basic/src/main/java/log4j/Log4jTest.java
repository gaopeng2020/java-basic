package log4j;

import org.apache.logging.log4j.*;

/**
 * @author gaopeng
 */
public class Log4jTest implements ILoggerLog4j {
    //	private static final Logger logger =LogManager.getLogger(Log4jTest.class);
    public static void main(String[] args) {
        Logger logger1 = ILoggerLog4j.getLogger("C:\\Users\\Public\\Documents\\gitlab\\java\\basic\\src\\main\\resources\\Log4j2-test.xml");
        for (int i = 0; i < 10; i++) {
            logger1.debug("调试debug信息");
            logger1.info("普通Info信息");
            logger1.warn("警告warn信息");
            logger1.error("error信息");
            logger.fatal("严重错误fatal信息");
            logger.trace("------------------------------------------------\n");
        }
    }
}
