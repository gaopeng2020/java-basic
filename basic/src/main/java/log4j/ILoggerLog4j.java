package log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author gaopeng
 */
public interface ILoggerLog4j {
    Logger logger = LogManager.getLogger(Log4jTest.class);
}
