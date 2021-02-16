package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCUnils {
	//private JDBCUnils() {}
	private static Connection con;
	
	static {
		try {
			//注册驱动
			Class.forName("com.mysql.jdbc.Driver");
			//连接数据库
			String url = "jdbc:mysql://WIN-PREEVISION:3306/mydb";
			String user = "root";
			String password = "123456";
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new RuntimeException(e+"Failed to connect to MySQL database");
		}
	}
	
	public static Connection getconnection() {
		return con;
	}
	
	
	public static void close(Connection con,Statement sta,ResultSet rs) {
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
	
	public static void close(Connection con,Statement sta) {
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
