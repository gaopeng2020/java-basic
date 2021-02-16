package basic.iostream;

import java.io.Serializable;

public class SerializablePerson implements Serializable {
	private String name;
	private transient int age;
	private transient String sex;
	//该类自定义序列号，编译器不会计算序列号
	static final long serialVersionUID = 42L;
	
	public SerializablePerson(String name, int age, String sex) {
		super();
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	public SerializablePerson() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "PersonSerializable [name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}
	
}
