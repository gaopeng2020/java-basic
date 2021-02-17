package multhread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/18 1:14
 * @description：
 * @modified By：
 * @version: $
 */
public class ThreadPool implements Runnable{
    /**
     * JDK 5.0起提供了线程池相关API：ExecutorServise和Executors ExecutorService：真正的线程池接口。
     * 常见子类ThreadPoolExecutor void execute（Runnable command）：执行任务/命令，没有返回值，一般用来执行Runnable
     * <T> Future<T> submit（Callable<T> task）：执行任务，有返回值，一般又来执行Callable void shutdown（）：关闭连接池
     * Executors：工具类、线程池的工厂类，用于创建并返回不同类型的线程池
     */
    private int ticketNum = 100;
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
        System.out.println(Thread.currentThread().getName() + " 买到了一张票，还剩余：" + ticketNum-- + "张票");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ThreadPool thread = new ThreadPool(100);
        executorService.execute(thread);
        executorService.execute(thread);
        executorService.execute(thread);
        executorService.execute(thread);
        executorService.execute(thread);

        //关闭池子
        executorService.shutdown();
    }
}
