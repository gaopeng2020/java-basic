package proxySkeleton.client;

import proxySkeleton.service.Person;

public class clientExe {

	public static void main(String[] args) {
		try {
			Person person = new PersonProxy();
			int age = person.getAge();
			String name = person.getName();
			System.out.println(name + " is " + age + " years old");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
