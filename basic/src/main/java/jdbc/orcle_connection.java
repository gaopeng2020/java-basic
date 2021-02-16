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

//			String url = "jdbc:oracle:thin:@db-preevision:1521:orcl"; // SID based URL
			String url = "jdbc:oracle:thin:@//db-preevision:1521/service_orcl"; // service based URL
			
			String user = "PREEVISION";
			String password = "PREEVISION";
			conn = DriverManager.getConnection(url, user, password);
			if (conn != null) {
				System.out.println("数据库链接成功");
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
				System.out.println("���ݿ��ѹرգ�\n");
			}
		} catch (Exception e) {
			System.out.println("���ݿ�ر�ʧ�ܣ�");
			e.printStackTrace();
		}
	}
}
