package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class JDBCDemo {

	public static void main(String[] args) throws ClassNotFoundException,SQLException, IOException {
//		
		//Connection con = JDBCUnils.getconnection();//无加载配置文件的连接方法
		Connection con=JDBCUnilsConfig.getconnection();//加载配置文件的连接方法
		databseupdata(con);
		databsequery(con);
		databasecannotbeattacted(con); //用PreparedStatement(sql)连接数据库，无法注入
		//用Statement(sql)连接数据库，可以通过注入语句在不满足查询条件时就能看到数据表全部内容
		databasecanbeattacted(con); 
		StorageDataToJavaFromMysql(con);
	}


	private static void StorageDataToJavaFromMysql(Connection con) throws SQLException {
		String sql = "SELECT * FROM zhangwu";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		List<ReceiveDataFromMySQL> list = new ArrayList<ReceiveDataFromMySQL>();
		while(rs.next()) {
			int sid = rs.getInt("id");
			String sname = rs.getString("NAME");
			double sprice = rs.getDouble("money");
			ReceiveDataFromMySQL rd = new ReceiveDataFromMySQL(sid, sname, sprice);
			list.add(rd);
		}
		for(ReceiveDataFromMySQL data:list) {
			System.out.println(data);
		}
		
	}

	private static void databseupdata(Connection con) throws SQLException {
		//prepareStatement方法必须采用占位符？，然后再通过setObject方法赋值。
		//Modify/update data
		String sql = "UPDATE usheet SET phone=? WHERE uid=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setObject(1, "18887654333");
		pst.setObject(2, 2);
		pst.executeUpdate();
		
		//Insert data
		String sql2 = "INSERT INTO usheet(uname,age,addrese,email,phone,occupation) "
				+ "VALUES(?,?,?,?,?,?),(?,?,?,?,?,?)";
		PreparedStatement pst2 = con.prepareStatement(sql2);
		pst2.setObject(1, "Dog");
		pst2.setObject(2, 2);
		pst2.setObject(3, "UK");
		pst2.setObject(4, "dog.uk@dog.en");
		pst2.setObject(5, "12345678900");
		pst2.setObject(6, "animal");
		
		pst2.setObject(7, "Dog");
		pst2.setObject(8, 2);
		pst2.setObject(9, "UK");
		pst2.setObject(10, "dog.uk@dog.en");
		pst2.setObject(11, "12345678900");
		pst2.setObject(12, "animal");
		pst2.executeUpdate();
		
		//Delete data
		String sql3 = "DELETE FROM usheet WHERE uid = ?";
		PreparedStatement pst3 = con.prepareStatement(sql3);
		pst3.setObject(1, 10);
		pst3.executeUpdate();
		
		JDBCUnils.close(con, pst);
	}

	private static void databsequery(Connection con) throws SQLException {
		String sql = "SELECT * FROM usheet";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString("uname")+"***"+rs.getString("age")+"***"+
		rs.getString("addrese")+"***"+rs.getString("phone")+"***"+rs.getString("email"));
			}
		JDBCUnils.close(con, pst, rs);
		}

	private static void databasecannotbeattacted(Connection con) throws SQLException {
		Scanner sc = new Scanner(System.in);
		String sid = sc.nextLine();
		String sname =sc.nextLine();
		String sql = "SELECT * FROM zhangwu WHERE id=? AND money=?";
		
		PreparedStatement sta = con.prepareStatement(sql);
		sta.setObject(1, sid);
		sta.setObject(2, sname);
		ResultSet rs = sta.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString("id")+"***"+rs.getString("name")
			+"***"+rs.getString("money"));
		}
		JDBCUnils.close(con, sta, rs);
	}
	
	private static void databasecanbeattacted(Connection con) throws SQLException {
		//在输入任意两个字符串，在第二个字符串后面跟上'or'1=1就会显示数据表全部内容。
		Scanner sc = new Scanner(System.in);
		String id = sc.nextLine();
		String name =sc.nextLine();
		String sql = "SELECT * FROM zhangwu WHERE id='"+id+"' AND NAME='"+name+"'";
		Statement sta = con.createStatement();
		ResultSet rs = sta.executeQuery(sql);
		while(rs.next()) {
			System.out.println(rs.getString("id")+rs.getString("name")+rs.getString("money"));
		}
		JDBCUnils.close(con, sta, rs);
	}

	
}
