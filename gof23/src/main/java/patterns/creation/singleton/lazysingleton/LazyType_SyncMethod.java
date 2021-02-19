package patterns.creation.singleton.lazysingleton;

/**
 * 单例模式有八种方式：
 * 1）饿汉式（静态常量）
 * 2）饿汉式（静态代码块）
 * 3）懒汉式（线程不安全）
 * 4）懒汉式（线程安全，同步方法）√
 * 5）懒汉式（线程安全，同步代码块）
 * 6）双重检查
 * 7）静态内部类
 * 8）枚举
 */

/**
 * 优缺点说明：
 * 1）解决了线程不安全问题
 * 2）效率太低了，每个线程在想获得类的实例时候，执行getInstance（）方法都要进行同步，
 * 而其实这个方法只执行一次实例化代码就够了，后面的想获得该类实例，直接return就行了。方法进行同步效率太低
 * 3）结论：在实际开发中，不推荐使用这种方式
 */
public class LazyType_SyncMethod {

    public static void main(String[] args) {
        System.out.println("懒汉式2 ， 线程安全~");
        Singleton_LazyType_SyncMethod instance = Singleton_LazyType_SyncMethod.getInstance_SyncMethod();
        Singleton_LazyType_SyncMethod instance2 = Singleton_LazyType_SyncMethod.getInstance_SyncMethod();
        System.out.println(instance == instance2); // true
        System.out.println("instance.hashCode=" + instance.hashCode());
        System.out.println("instance2.hashCode=" + instance2.hashCode());
    }

}

// 懒汉式(线程安全，同步方法)
class Singleton_LazyType_SyncMethod {
    private static Singleton_LazyType_SyncMethod instance;

    private Singleton_LazyType_SyncMethod() {
    }

    //提供一个静态的公有方法，加入同步处理的代码，解决线程安全问题
    //即懒汉式
    public static synchronized Singleton_LazyType_SyncMethod getInstance_SyncMethod() {
        if (instance == null) {
            instance = new Singleton_LazyType_SyncMethod();
        }
        return instance;
    }

    //同步代码块
    public static Singleton_LazyType_SyncMethod getInstance_syncBlock() {
        synchronized (Singleton_LazyType_SyncMethod.class) {
            if (instance == null) {
                instance = new Singleton_LazyType_SyncMethod();
            }
            return instance;
        }
    }
}