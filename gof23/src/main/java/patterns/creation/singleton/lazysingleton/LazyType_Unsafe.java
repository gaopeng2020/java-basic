package patterns.creation.singleton.lazysingleton;
/**
 * 单例模式有八种方式：
 * 1）饿汉式（静态常量）
 * 2）饿汉式（静态代码块）
 * 3）懒汉式（线程不安全）√
 * 4）懒汉式（线程安全，同步方法）
 * 5）懒汉式（线程安全，同步代码块）
 * 6）双重检查
 * 7）静态内部类
 * 8）枚举
 */

/**
 * 优缺点说明：
 * 1）起到了Lazy Loading的效果，但是只能在单线程下使用。
 * 2）如果在多线程下，一个线程进入了if（singleton-s nul）判断语句块，还未来得及往下执行，
 *    另一个线程也通过了这个判断语句，这时便会产生多个实例。所以在多线程环境下不可使用这种方式
 * 3）结论：在实际开发中，不要使用这种方式.
 */
public class LazyType_Unsafe {

	public static void main(String[] args) {
		System.out.println("懒汉式1 ， 线程不安全~");
		Singleton_LazyType_SyncMethod instance = Singleton_LazyType_SyncMethod.getInstance_SyncMethod();
		Singleton_LazyType_SyncMethod instance2 = Singleton_LazyType_SyncMethod.getInstance_SyncMethod();
		System.out.println(instance == instance2); // true
		System.out.println("instance.hashCode=" + instance.hashCode());
		System.out.println("instance2.hashCode=" + instance2.hashCode());
	}

}

class Singleton_LazyType_Unsafe {
	private static Singleton_LazyType_Unsafe instance;
	
	private Singleton_LazyType_Unsafe() {}
	
	//提供一个静态的公有方法，当使用到该方法时，才去创建 instance
	//即懒汉式
	public static Singleton_LazyType_Unsafe getInstance() {
		if(instance == null) {
			instance = new Singleton_LazyType_Unsafe();
		}
		return instance;
	}
}