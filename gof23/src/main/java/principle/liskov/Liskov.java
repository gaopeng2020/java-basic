package principle.liskov;

/**
 * 里氏替换原则:
 * 1）里氏替换原则（Liskov Substitution Principle）在1988年，由麻省理工学院的以为姓里的女士提出的。
 * 2）如果对每个类型为T1的对象01，都有类型为T2的对象02，使得以T1定义的所有程序P在所有的对象01都代换成02时，程序P的行为没有发生变化，那么类型T2是类型T1的子类型。
 * 换句话说，所有引用基类的地方必须能透明地使用其子类的对象。
 * 3）在使用维承时，遵循里氏替换原则，在子类中尽量不要重写父类的方法
 * 4）里氏替换原则告诉我们，维承实际上让两个类耦合性增强了，在适当的情况下，可以通过聚合，组合，依赖来解决问题.
 */
public class Liskov {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		A a = new A();
		System.out.println("11-3=" + a.func1(11, 3));
		System.out.println("1-8=" + a.func1(1, 8));

		System.out.println("-----------------------");
		B b = new B();
		System.out.println("11-3=" + b.func1(11, 3));//这里本意是求出11-3
		System.out.println("1-8=" + b.func1(1, 8));// 1-8
		System.out.println("11+3+9=" + b.func2(11, 3));
	}

}

// A类
class A {
	// 返回两个数的差
	public int func1(int num1, int num2) {
		return num1 - num2;
	}
}

// B类继承了A
// 增加了一个新功能：完成两个数相加,然后和9求和
class B extends A {
	//这里，重写了A类的方法, 可能是无意识
	public int func1(int a, int b) {
		return a + b;
	}

	public int func2(int a, int b) {
		return func1(a, b) + 9;
	}
}
