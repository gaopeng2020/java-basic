package basic.thread.pool;

import java.util.concurrent.Callable;

//Callable接口：与Runnable接口功能相似，用来指定线程的任务。重写的call()方法，用来返回线程任务执行完毕后的结果，call方法可抛出异常。

public class ThreadPoolDemo implements Callable<Integer> {
	private int a;
	public ThreadPoolDemo(int a){
		this.a=a;
	}
	
	@Override
	public Integer call() throws Exception {
		int sum = 0 ;
		for(int i = 1 ; i <=a ; i++){
			sum = sum + i ;
		}
		return sum;
	}

}
