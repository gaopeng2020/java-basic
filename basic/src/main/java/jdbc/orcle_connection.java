package jdbc;

import java.sql.*;

public class orcle_connection {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		orcle_connection getConn = new orcle_connection();
		Connection connection = getConn.openConnection();
		
		try {
			Thread.currentThread();
			Thread.sleep(5000);
			getConn.closeConnection(connection);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 建立oracle数据库连接
	 */
	public Connection openConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

//			String url = "jdbc:oracle:thin:@180.168.184.107:8088:orcl"; // SID based URL
			String url = "jdbc:oracle:thin:@//180.168.184.107:8088/preevision"; // service based URL
			
			String user = "PREEVISION95";
			String password = "PREEVISION95";
			conn = DriverManager.getConnection(url, user, password);
			if (conn != null) {
				System.out.println("数据库连接成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 *
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				System.out.println("数据库关闭成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
