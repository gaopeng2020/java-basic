package netty.demos;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.*;

public class FuturePromiseTest {
    public static void main(String[] args) {
        /**
         netty 的 Future 继承自 jdk 的 Future，而 Promise 又对 netty Future 进行了扩展
             jdk Future 只能同步等待任务结束（或成功、或失败）才能得到结果
             netty Future 可以同步等待任务结束得到结果，也可以异步方式得到结果，但都是要等任务结束
             netty Promise 不仅有 netty Future 的功能，而且脱离了任务独立存在，只作为两个线程间传递结果的容器
         */
        jdkFutureTest();

        nettyFutureTest();

        nettyPromiseTest(0);
    }

    private static void nettyPromiseTest(int divider) {
        // 创建EventLoop
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        // 创建Promise对象，用于存放结果
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        //创建一个新线程模拟对端，用于向promise中设置结果
        new Thread(() -> {
            try {
                int i = 1 / divider;
                TimeUnit.SECONDS.sleep(1);
                promise.setSuccess(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
            // 自定义线程向Promise中存放结果

        }).start();

        // 主线程从Promise中获取结果
        try {
            System.out.println(Thread.currentThread().getName() + " " + promise.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void nettyFutureTest() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 获得 EventLoop 对象，即一个线程对象
        EventLoop eventLoop = group.next();
        // eventLoop.submit()方法获得Future对象
        io.netty.util.concurrent.Future<Integer> future = eventLoop.submit(() -> 50);

        // getNow方法，获取结果，若还没有结果，则返回null，该方法是非阻塞的
        System.out.println("getNow " + future.getNow());

        //get方法，同步阻塞的方式获取返回结果
        try {
            System.out.println("同步阻塞的方式获取返回结果 " + future.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // NIO线程中异步获取结果
        future.addListener(future1 -> System.out.println("异步方式获取返回结果 = " + future1.get()));
    }

    private static void jdkFutureTest() {
        ThreadFactory factory = runnable -> new Thread(runnable, "JdkFuture");
        // 创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                10,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), factory);
        //也可以通过Executors的静态方法创建executor对象，ThreadPoolExecutor与ExecutorService都实现了Executor接口
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // executor.submit()方法获得Future对象
        Future<Integer> future = executor.submit(() -> {
            TimeUnit.SECONDS.sleep(1);
            return 50;
        });

        // 通过future.get()阻塞的方式，获得运行结果
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
