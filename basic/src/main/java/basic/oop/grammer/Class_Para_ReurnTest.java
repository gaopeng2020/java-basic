package basic.oop.grammer;

public class Class_Para_ReurnTest {
	public static void main(String[] args) {
		//调用传完参的方法，该方法有个类型位类的形参，因此需要新建一个类对象
		Class_Para_Reurn pa = new Class_Para_Reurn();
		paraTest(pa);
		
		//创建一个"方法对象"，调用源类中的方法show()
		Class_Para_Reurn re = returnTest();
			re.show();
		
	}
	//类作为方法参数传递,传参后的方法具备了原类的功能。
	public static void paraTest(Class_Para_Reurn para) {
		para.show();
	}
	
	//类作为方法返回值。返回值还是类，在被调用时需要新建“方法对象”
	public static Class_Para_Reurn returnTest() {
		Class_Para_Reurn r = new Class_Para_Reurn();
		return r;
	}
	
}
