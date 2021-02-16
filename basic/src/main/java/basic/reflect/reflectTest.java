package basic.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class reflectTest {

	public static void main(String[] args) {
		person p = new person();
		Class c1= p.getClass();
		Class c2 = person.class;
		Class c3 = null;
		try {
			c3=Class.forName("Reflect.person");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String str = "a123";
		if(str.equals("a123")&&str.equals("a123")) {
			System.out.println(str);
		}
//		setValueByVariable(p);
//		getConstructor(c1);
//		getMenberVariable(c2,p);
//		getMerberMethod(c3);
	}

	private static void setValueByVariable(person p) {
		int age = p.getAge();
		age = 20;
		System.out.println(p);
	}

	private static void getConstructor(Class c1) {
		try {
			//获取所有构造方法
			Constructor[] constructorAll = c1.getDeclaredConstructors();
			for(Constructor con1:constructorAll) {
				System.out.println(con1);
			}
			//获取指定参数类型的私有构造方法
			Constructor constructor1 = c1.getDeclaredConstructor(int.class,String.class);
			System.out.println(constructor1);
			//获取指定参数类型的公有构造方法
			Constructor constructor = c1.getConstructor(String.class,int.class,String.class);
			System.out.println(constructor);
			//通过获取的构造方法创建对象
			try {
				Object instance = constructor.newInstance("小明", 22, "哈尔滨");
				System.out.println(instance);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getMenberVariable(Class c2, person p) {
		//获取所有成员变量
		Field[] fields = c2.getDeclaredFields();
		for(Field f : fields) {
			System.out.println(f);
		}
		//获取指定类型的私有成员变量
		try {
			Field declaredField = c2.getDeclaredField("age");
			System.out.println(declaredField);
			//给获取的私有成员变量赋值；不需要通过成员方法。
			try {
				//setAccessible(true)：暴力访问私有成员变量
				declaredField.setAccessible(true);
				declaredField.set(p, 30);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		//获取指定类型的公有成员变量
		try {
			Field field = c2.getField("name");
			System.out.println(field);
			//给获取的成员变量赋值；
			try {
				field.set(p, "小雷");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		System.out.println(p);
	}

	private static void getMerberMethod(Class c3) {
		Method[] methods = c3.getDeclaredMethods();
		for(Method m:methods) {
			System.out.println(m);
		}
	}
	

}
