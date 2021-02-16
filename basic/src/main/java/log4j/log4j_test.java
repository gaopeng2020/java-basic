package log4j;

import org.apache.log4j.Logger;

public class log4j_test {
	private static Logger logger=Logger.getLogger(log4j_test.class); // 获取logger实例 

    public static void main(String[] args) {

        logger.debug("调试debug信息");
        logger.info("普通Info信息");
        logger.warn("警告warn信息");
        logger.error("error信息");       
        logger.fatal("严重错误fatal信息");
    }
}
