package netty.simpledemos;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

//处理普通与定时任务
@Log4j2
public class TestEventLoop {
    public static void main(String[] args) {
        // 创建拥有两个EventLoop的NioEventLoopGroup，对应两个线程
        EventLoopGroup group = new NioEventLoopGroup(2);
        // 通过next方法可以获得下一个 EventLoop
        System.out.println(group.next());
        System.out.println(group.next());
        log.info(group.next());
        log.info(group.next());

        // 通过EventLoop执行普通任务
        group.next().execute(()->{
//            System.out.println(Thread.currentThread().getName() + " hello");
            log.info(Thread.currentThread().getName() + " 普通任务");
        });

        // 通过EventLoop执行定时任务,执行定时任务，group不可关闭
        group.next().scheduleAtFixedRate(()->{
//            System.out.println(Thread.currentThread().getName() + " hello2");
            log.info(Thread.currentThread().getName() + " 定时任务");
        }, 0, 1, TimeUnit.SECONDS);

        // 优雅地关闭
//        group.shutdownGracefully();
    }
}
