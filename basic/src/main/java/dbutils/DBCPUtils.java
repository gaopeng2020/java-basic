package dbutils;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.InputStream;
import java.util.Properties;


public class DBCPUtils {
	
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	private static BasicDataSource ds;
	
	private static String maxActive;
	private static String minIdle;
	private static String maxIdle;
	private static String initialSize;
	
	static {
		try {
			InputStream is =QueryRunnerUpdate.class.getClassLoader().getResourceAsStream("DatabaseConfig.properties");
			Properties pro = new Properties();
			pro.load(is);
			driver=pro.getProperty("driver");
			url=pro.getProperty("url");
			user=pro.getProperty("user");
			password=pro.getProperty("password");
			
			maxActive=pro.getProperty("maxActive");
			minIdle=pro.getProperty("minIdle");
			maxIdle=pro.getProperty("maxIdle");
			initialSize=pro.getProperty("initialSize");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e+"配置文件读取失败");
		}
	}
	
	public static BasicDataSource getconnection(){
		try {
			ds=new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(password);
			ds.setMaxActive(Integer.parseInt(maxActive));
			ds.setMinIdle(Integer.parseInt(minIdle));
			ds.setMaxIdle(Integer.parseInt(maxIdle));
			ds.setInitialSize(Integer.parseInt(initialSize));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}
}
