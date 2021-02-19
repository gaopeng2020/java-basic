package patterns.creation.singleton.doubleCheck;
/**
 * 单例模式有八种方式：
 * 1）饿汉式（静态常量）
 * 2）饿汉式（静态代码块）
 * 3）懒汉式（线程不安全）
 * 4）懒汉式（线程安全，同步方法）
 * 5）懒汉式（线程安全，同步代码块）
 * 6）双重检查 √
 * 7）静态内部类
 * 8）枚举
 */

/**
 * 优缺点说明：由懒汉式演变而来
 * 1）Double-Check概念是多线程开发中常使用到的，如代码中所示，我们进行了两次if（singletonsnull1查，这样就可以保证线程安全了。
 * 2）这样，实例化代码只用执行一次，1后面再次访问时，判断if（singleton == null），直接return实例化对象，也避免的反复进行方法同步.
 * 3）线程安全；延迟加载；效率较高
 * 4）结论：在实际开发中，推荐使用这种单例设计模式
 */

public class DoubleCheck {

    public static void main(String[] args) {
        System.out.println("线程安全，双重检查");
        Singleton_DoubleCheck instance = Singleton_DoubleCheck.getInstance();
        Singleton_DoubleCheck instance2 = Singleton_DoubleCheck.getInstance();
        System.out.println(instance == instance2); // true
        System.out.println("instance.hashCode=" + instance.hashCode());
        System.out.println("instance2.hashCode=" + instance2.hashCode());

    }

}

// 懒汉式(线程安全，同步方法)
class Singleton_DoubleCheck {
    //定义一个私有的对象实例
    private static volatile Singleton_DoubleCheck instance;

    //定义一个私有的构造器
    private Singleton_DoubleCheck() {
    }

    //提供一个静态的公有方法，加入双重检查代码，解决线程安全问题, 同时解决懒加载问题
    //同时保证了效率, 推荐使用

    public static synchronized Singleton_DoubleCheck getInstance() {
        if (instance == null) {
            synchronized (Singleton_DoubleCheck.class) {
                if (instance == null) {
                    instance = new Singleton_DoubleCheck();
                }
            }

        }
        return instance;
    }
}