package multhread;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 20:25
 * @description：lamda implementation test
 * @modified By：
 * @version: 1.0$
 */
public class LambdaImpl implements Lambda {
    //实现方式一：通过实现类
    @Override
    public void lambdaTest(int i, String str) {
        System.out.println("i = " + i);
        System.out.println("str = " + str);
    }

    //实现方式二：静态内部类
    static class LambdaImpl_StaticInternalClass implements Lambda {
        //实现方式一：通过实现类
        @Override
        public void lambdaTest(int i, String str) {
            System.out.println("i = " + i);
            System.out.println("str = " + str);
        }
    }

    public static void main(String[] args) {
        //实现方式一：通过实现类
        Lambda lambda = new LambdaImpl();
        lambda.lambdaTest(1, "Hello Lambda");
        System.out.println("========================================");

        //实现方式二：静态内部类
        lambda = new LambdaImpl_StaticInternalClass();
        lambda.lambdaTest(2, "Hello Lambda");
        System.out.println("========================================");

        //实现方式三：局部内部类
        class LambdaImpl_InternalClass implements Lambda {
            //实现方式一：通过实现类
            @Override
            public void lambdaTest(int i, String str) {
                System.out.println("i = " + i);
                System.out.println("str = " + str);
            }
        }
        lambda = new LambdaImpl_InternalClass();
        lambda.lambdaTest(3, "Hello Lambda");
        System.out.println("========================================");

        //实现方式四：匿名内部类(没有类名，必须借助接口或者服装来创建).直接new 接口即可
        lambda = new Lambda() {
            @Override
            public void lambdaTest(int i, String str) {
                System.out.println("i = " + i);
                System.out.println("str = " + str);
            }
        };
        lambda.lambdaTest(4, "Hello Lambda");
        System.out.println("========================================");

        //实现方式五：lambda
        /**
         * lambda表达式由内部匿名函类演变而来，只保留了它（）和{}内部的内容
         * ()里面的参数是接口函数的形参，参数可以带类型声明，也可以不要
         * {}里面是对接口方法的实现
         * 如果{}内部只有一行代码，则可以将大括号去掉
         */
        lambda = (i, str) -> {
            System.out.println("i = " + i);
            System.out.println("str = " + str);
        };
        lambda.lambdaTest(5, "Hello Lambda");
        System.out.println("========================================");

        /**
         * 如果{}内部只有一行代码，则可以将大括号去掉;
         * 如果函数的参数只有一个，则可以将括号也去掉
         */
        lambda = (i, str) -> System.out.println("i = " + i + "\nstr = " + str);
        lambda.lambdaTest(6, "Hello Lambda");

    }

}
