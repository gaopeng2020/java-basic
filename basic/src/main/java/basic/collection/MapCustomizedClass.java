package basic.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import basic.oop.constructor.Person;

public class MapCustomizedClass {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		Map<Person,String> map = new HashMap<Person,String>();
		map.put(new Person("小明",23), "北京");
		map.put(new Person("小红",22), "上海");
		map.put(new Person("小绿",25), "深圳");
		map.put(new Person("小哈",26), "杭州");
		map.put(new Person("小嘻",25), "南京");
		map.put(new Person("小嘻",23), "南京");

		System.out.println("#######使用增强for循环和keyset方法######");
		for(Person key :map.keySet()) {
			System.out.println(key+"+++"+map.get(key));
		}
		
		System.out.println("#######使用增强for循环和entryset方法######");
		for(Map.Entry<Person, String> entry :map.entrySet()) {
			System.out.println(entry.getKey()+"***"+entry.getValue());
		}
		
		System.out.println("#######使用Iterator和entryset方法#######");
		Set<Map.Entry<Person, String>> set = map.entrySet();
		Iterator<Map.Entry<Person, String>> it = set.iterator();
		while(it.hasNext()) {
			Map.Entry<Person, String> entry = it.next();
			System.out.println(entry.getKey()+"***"+entry.getValue());
		}
	}
	
}

/*class Person{
	private String name;
	private int age;
	

	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
		public Person() {
		super();
		// TODO Auto-generated constructor stub
		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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

	
	@Override
	public String toString() {
		return "name=" + name + ", age=" + age + ",";
	}
	
}*/
