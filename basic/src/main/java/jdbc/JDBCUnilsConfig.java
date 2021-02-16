package jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUnilsConfig {
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	private static Connection con;
	
	static {
		try {
			InputStream is =JDBCDemo.class.getClassLoader().getResourceAsStream("DatabaseConfig.properties");
			Properties pro = new Properties();
			pro.load(is);
			
			driver = pro.getProperty("driver");
			url=pro.getProperty("url");
			user=pro.getProperty("user");
			password=pro.getProperty("password");
		} catch (Exception e) {
			throw new RuntimeException(e+"配置文件读取失败");
		}
	}
	
	//定义静态方法 getconnection
	public static Connection getconnection() {
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
			if(con!=null) System.out.println("数据库 "+url+"连接成功");
			return con;
		} catch (Exception e) {
			throw new RuntimeException(e+"数据库连接失败");
		}
		
	}
	
	public void close(Connection con,Statement sta,ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (Exception e) {}
		}
		
		if(sta!=null) {
			try {
				sta.close();
			} catch (Exception e) {}
		}
		
		if(con!=null) {
			try {
				con.close();
			} catch (Exception e) {}
		}
				
	}
	
	public void close(Connection con,Statement sta) {
		if(sta!=null) {
			try {
				sta.close();
			} catch (Exception e) {}
		}
		
		if(con!=null) {
			try {
				con.close();
			} catch (Exception e) {}
		}
		
	}
	
}
