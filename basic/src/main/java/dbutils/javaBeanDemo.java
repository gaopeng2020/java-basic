package dbutils;
/*
 * 1.	JavaBean就是一个类，在开发中常用封装数据
 * 2.	提供私有字段：private 类型 字段名;
 * 3.	提供getter/setter方法
 * 4.	提供无参构造
 * */

public class javaBeanDemo {
	private int id;
	private String name;
	private String password;
	private String createDate;

	public javaBeanDemo() {
		super();
	}

	public javaBeanDemo(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
	public javaBeanDemo(String name, String password, String createDate) {
		super();
		this.name = name;
		this.password = password;
		this.createDate = createDate;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Administrator [id=" + id + ", " + (name != null ? "name=" + name + ", " : "")
				+ (password != null ? "password=" + password + ", " : "")
				+ (createDate != null ? "createDate=" + createDate : "") + "]";
	}

	
}

