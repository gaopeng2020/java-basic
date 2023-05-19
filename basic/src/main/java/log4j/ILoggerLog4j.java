package log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.nio.file.Files;

/**
 * @author gaopeng
 */
public interface ILoggerLog4j {
    Logger logger = LogManager.getLogger(ILoggerLog4j.class);

    /**
     * get Logger by specified configuration of log4j2
     *
     * @param log4jConfPath log4j2 Configuration Path
     * @return Logger
     */
    static Logger getLogger(String log4jConfPath) {
        File log4jFile = new File(log4jConfPath);
        try {
            if (log4jFile.exists()) {
                ConfigurationSource source = new ConfigurationSource(Files.newInputStream(log4jFile.toPath()), log4jFile);
                Configurator.initialize(null, source);
                return LogManager.getLogger("Log4j2-test");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }


}
