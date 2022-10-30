package dbutils;

import jdbc.JDBCUnilsConfig;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
/*
 * 对数据库操作的两种方法：
 * 1. PreparedStatement pst = con.prepareStatement(sql),
 * 		pst.setObject()的方法对数据表增删改。
 * 		ResultSet rs = sta.executeQuery(sql)对数据库查询
 * 		也可以用？代替sql中的value。
 * 
 * 2. 用第三方工具DBUtils的QueryRunner工具的update与query的方法对数据操作
 * 		new QueryRunner().update(conn,sql,params)实现对表数据的增删改操作
 * 		ResultSetHandler<Object[]> rsh=new 结果处理集合（8种）实现对数据表查询
 * 		params要求sql语句的value用？代替
 * */
public class QueryRunnerUpdate {
public static void main(String[] args) throws SQLException {
	//采用JDBCUtils的方法连接数据库
	Connection con = JDBCUnilsConfig.getconnection();
	fQueryRunnerUpdate1(con);
	
	 //采用DBCPUtil连接池的方法连接数据库，
	DataSource dbc = DBCPUtils.getconnection();
	fQueryRunnerUpdate1(dbc);

}

private static void fQueryRunnerUpdate1(DataSource dbc) {
	QueryRunner qr = new QueryRunner();
	String sql ="INSERT INTO zhangwu (NAME,money) values (?,?),(?,?),(?,?)";
	Object[] params = {"旅游指出",3000,"交通费用",500,"电话费用",100};
	try {
		qr.update(sql, params);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	String sql2 = "UPDATE zhangwu SET NAME=?,money=? WHERE id=?";
	Object[] params2 = {"旅游支出",3800,15};
	try {
		qr.update(sql2, params2);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	String sql3 ="DELETE FROM zhangwu WHERE id=? OR id=?";
	Object[] params3 = {16,17};
	try {
		qr.update(sql3, params3);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	//DBCPUtils连接池无需关闭Connection、Statement、ResultSet
}

private static void fQueryRunnerUpdate1(Connection con) throws SQLException {
	QueryRunner qr = new QueryRunner();
	String sql ="INSERT INTO zhangwu (NAME,money) values (?,?),(?,?),(?,?)";
	Object[] params = {"旅游指出",3000,"交通费用",500,"电话费用",100};
	qr.update(con, sql, params);
	
	String sql2 = "UPDATE zhangwu SET NAME=?,money=? WHERE id=?";
	Object[] params2 = {"旅游支出",3800,15};
	qr.update(con,sql2, params2);
	
	String sql3 ="DELETE FROM zhangwu WHERE id=? OR id=?";
	Object[] params3 = {16,17};
	qr.update(con, sql3, params3);
	
	DbUtils.closeQuietly(con);
}
}
