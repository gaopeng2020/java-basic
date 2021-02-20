package patterns.structure.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Cglib代理模式的基本介绍
 * 1）静态代理和JDK代理模式都要求目标对象是实现一个接口，但是有时候目标对象只是一个单独的对象，并没有实现任何的接口，这个时候可使用目标对象子类来实现代理-这就是Cglib代理
 * 2）Cglib代理也叫作子类代理，它是在内存中构建一个子类对象从而实现对目标对象功能扩展，有些书也将Cglib代理归属到动态代理。
 * 3）Cglib是一个强大的高性能的代码生成包，它可以在运行期扩展java类与实现java接口.它广泛的被许多AOP的框架使用，例如Spring AOP，实现方法拦截
 * 4）在AP编程中如何选择代理模式：
 * 1，目标对象需要实现接口，用JDK代理
 * 2.目标对象不需要实现接口，用Cglib代理
 * 5）Cglib包的底层是通过使用字节码处理框架ASM来转换字节码并生成新的类
 */
public class ProxyFactory implements MethodInterceptor {

    //维护一个目标对象
    private Object target;

    //构造器，传入一个被代理的对象
    public ProxyFactory(Object target) {
        this.target = target;
    }

    //返回一个代理对象:  是 target 对象的代理对象
    public Object getProxyInstance() {
        //1. 创建一个工具类
        Enhancer enhancer = new Enhancer();
        //2. 设置父类
        enhancer.setSuperclass(target.getClass());
        //3. 设置回调函数
        enhancer.setCallback(this);
        //4. 创建子类对象，即代理对象
        return enhancer.create();
    }


    //重写  intercept 方法，会调用目标对象的方法
    @Override
    public Object intercept(Object arg0, Method method, Object[] args, MethodProxy arg3) throws Throwable {
        // TODO Auto-generated method stub
        System.out.println("Cglib代理模式 ~~ 开始");
        Object returnVal = method.invoke(target, args);
        System.out.println("Cglib代理模式 ~~ 提交");
        return returnVal;
    }

}
