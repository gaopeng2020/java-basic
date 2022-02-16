import ept.commonapi.EPTUtils;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 函数式接口（Functional Interface）：一个接口中只有一个抽象方法
 * lambda 表达式一般用于接口，且该接口只有一个抽象方法（可以有其他非抽象方法）。
 * 1.举例：（01，02）-> Integer.compare（01，02）；
 * 2.格式：()-> {}
 * ->左边：Lambda形参列表（其实就是接口中的抽象方法的形参列表）
 * ->右边：Lambda体（其实就是重写的抽象方法的方法体）
 * 3.Lambda表达式的语法格式：（分为3种情况介绍）
 *  a.无参数，无返回值：Runnable r2 = () -> System.out.println("Lambda：1QAZ");
 *  b.有参数，无返回值: Consumer<String> consumer2 = s->System.out.println("s = Lambda方法：" + s);
 *  c.有参数，有返回值：Comparator<Integer> com2 = (o1,o2) -> o1.compareTo(o2)
 * 4.Lambda表达式的本质：作为接口实现类的一个实例。
 * java内置四大核心函数式接口：
 *  Consumer<T>消费型接口，消费一个对象不返回，方法：accept（T t）
 *  Supplier<T> 供给型接口，提供一个对象,T get（）；
 *  Function<T,R> 对类型为T的对象应用操作，并返回类型为R的对象，R apply(T t)
 *  Predicate<T> 确定类型为T的 对象是否满足某约束条件，返回boolean, boolean test(T t)
 *
 * 当要传递给Lambda体的操作，已经有实现的方法了，可以使用方法引用
 */
public class LambdaTest {
    @Test
    //a.无参数，无返回值，如果大括号内部只有一行代码，则大括号也可以省略
    public void Test1() {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("传统方法：a.无参数，无返回值");
            }
        };
        r1.run();
        System.out.println("============================================");
        Runnable r2 = () -> System.out.println("Lambda：a.无参数，无返回值");
        r2.run();
    }

    @Test
    //b.有参数，无返回值：
    // 参数的数据类型可以省略，
    // 如果只有一个参数，则小括号也可以省略；
    public void test2() {
        Consumer<String> consumer1 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("s = 传统方法：" + s);
            }
        };
        consumer1.accept("b.有参数，无返回值");
        System.out.println("============================================");
        Consumer<String> consumer2 = s -> System.out.println("s = Lambda方法：" + s);
        consumer2.accept("b.有参数，无返回值");
    }

    @Test
    //c.有参数，有返回值（返回值通过方法体的return语句实现）
    //但大括号内只有一条语句时，return 与大括号都可以省略
    public void test3() {
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        System.out.println("com1.compare(12,22) = " + com1.compare(2, 22));

        System.out.println("============================================");
//        Comparator<Integer> com2 = (o1,o2) -> { return o1.compareTo(o2);};
        Comparator<Integer> com2 = (o1, o2) -> o1.compareTo(o2);
        //方法引用写法
//        Comparator<Integer> com2 = Integer::compare;
        System.out.println("com2.compare(12,22) = " + com2.compare(0, 22));
    }

    @Test
    public void supplierTest() {
        Consumer<Integer> consumer = num->System.out.println("您的输入的数字是："+ num);
        consumer.accept(123456);
    }

    @Test
    public void functionTest() {



    }

    @Test
    public void predicateTest() {
        String str1 = "ABC！@#123";
        String str2 = "ABED1234%^&*(";
       Predicate<String> pre1 = str-> str.contains("4");
        System.out.println("pre.test(str1) = " + pre1.test(str1));
        System.out.println("pre.test(str2) = " + pre1.test(str2));

//        Predicate<String> pre2 = str-> EPTUtils.isHexNumber(str);
        Predicate<String> pre2 = EPTUtils::isHexNumber;
        System.out.println("pre2.test(\"0xabcdef\") = " + pre2.test("0xabcdef"));
    }

}


