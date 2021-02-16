package basic.thread.pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 *  ExecutorService：线程池类
	<T> Future<T> submit(Callable<T> task)：获取线程池中的某一个线程对象，并执行线程中的call()方法；
	返回值 Future接口：用来记录线程任务执行完毕后产生的结果。线程池创建与使用

	使用线程池中线程对象的步骤：
		创建线程池对象
		创建Callable接口子类对象
		提交Callable接口子类对象
		关闭线程池
 * */

/*
 * 使用多线程技术,求和
 * 两个线程,1个线程计算1+100,另一个线程计算1+200的和
 * 多线程的异步计算
 */
public class main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newFixedThreadPool(2);
		Future<Integer> f1 =es.submit(new ThreadPoolDemo(100000));
		Future<Integer> f2 =es.submit(new ThreadPoolDemo(110000));
		System.out.println(f1.get());
		System.out.println(f2.get());
		
		es.shutdown();

	}

}
