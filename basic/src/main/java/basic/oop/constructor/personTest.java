package basic.oop.constructor;

public class personTest {

	public static void main(String[] args) {
		SubPerson p =new SubPerson("ping",18);
		p.setAge(28);
		p.setName("pingping");
		System.out.println(p);
	}

}
