package patterns.creation.singleton.hungrytype;

/**
 * 单例模式有八种方式：
 * 1）饿汉式（静态常量）
 * 2）饿汉式（静态代码块）√
 * 3）懒汉式（线程不安全）
 * 4）懒汉式（线程安全，同步方法）
 * 5）懒汉式（线程安全，同步代码块）
 * 6）双重检查
 * 7）静态内部类
 * 8）枚举
 */

/**
 * 优缺点说明：
 * 1）这种方式和上面的方式其实类似，只不过将类实例化的过程放在了静态代码块中，
 *   也是在类装载的时候，就执行静态代码块中的代码，初始化类的实例。优缺点和上面是一样的.
 * 2）结论：这种单例模式可用，但是可能造成内存浪费
 */
public class HungryType_StaticBlock {

    public static void main(String[] args) {
        //测试
        Singleton_StaticConstaint instance = Singleton_StaticConstaint.getInstance();
        Singleton_StaticConstaint instance2 = Singleton_StaticConstaint.getInstance();
        System.out.println(instance == instance2); // true
        System.out.println("instance.hashCode=" + instance.hashCode());
        System.out.println("instance2.hashCode=" + instance2.hashCode());
    }

}

//饿汉式(静态变量)

class Singleton_StaticBlock {

    //1. 构造器私有化, 外部能new
    private Singleton_StaticBlock() {

    }

    //2.本类内部创建对象实例
    private static Singleton_StaticBlock instance;

    static { // 在静态代码块中，创建单例对象
        instance = new Singleton_StaticBlock();
    }

    //3. 提供一个公有的静态方法，返回实例对象
    public static Singleton_StaticBlock getInstance() {
        return instance;
    }

}