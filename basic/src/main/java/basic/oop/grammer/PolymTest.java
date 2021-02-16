package basic.oop.grammer;

public class PolymTest {

	public static void test(String[] args) {
		PolymSupperClass polym = new PolymSubClass();
//			if(polym instanceof PolymSubClass) {
//			System.out.println("����polym��������");
//		}
		polym.method();
		PolymSupperClass.num=30;
		System.out.println(PolymSupperClass.num);
		System.out.println(PolymSubClass.num);
		
	}

}
