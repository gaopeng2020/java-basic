package multhread;

import java.util.concurrent.*;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/18 1:14
 */
public class ThreadPool implements Runnable{
    /**
     * JDK 5.0起提供了线程池相关API：ExecutorService和Executors, ExecutorService才是真正的线程池接口。
     * 常见子类ThreadPoolExecutor void execute（Runnable command）：执行任务/命令，没有返回值，一般用来执行Runnable
     * void shutdown（）：关闭连接池
     * <T> Future<T> submit（Callable<T> task）：执行任务，有返回值，一般又来执行Callable
     * Executors：工具类、线程池的工厂类，用于创建并返回不同类型的线程池
     */
    private int ticketNum;
    private boolean flag = true;

    public ThreadPool(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    @Override
    public void run() {
        while (flag) {
            buyTicket();
        }
    }

    private synchronized void buyTicket() {
        if (ticketNum < 0) {
            flag = false;
            return;
        }
        System.out.println(Thread.currentThread().getName() + " 买到了一张票，还剩余：" + --ticketNum + "张票");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ThreadPool thread1 = new ThreadPool(100);
        ThreadPool thread2 = new ThreadPool(200);
        ThreadPool thread3 = new ThreadPool(200);
        ThreadPool thread4 = new ThreadPool(300);
        ThreadPool thread5 = new ThreadPool(500);
        executorService.execute(thread1);
        executorService.execute(thread2);
        executorService.execute(thread3);
        executorService.execute(thread4);
        executorService.execute(thread5);

        //关闭池子
        executorService.shutdown();
    }
}
