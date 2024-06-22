package multhread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 22:52
 * @description：ReentrantLock Test
 * @modified By：
 * @version: 1.0$
 */
public class ReentrantLockTest2 implements Runnable {
    /**
     * java.util.concurrent.locks.Lock接口是控制多个线程对共享资源进行访问的工具。
     * Lock提供了对共享资源的独占访问，每次只能有一个线程对Lock对象加锁，线程开始访问共享资源之前应先获得Lock对象
     * ReentrantLock类实现了Lock，它拥有与synchronized相同的并发性和内存语义，在实现线程安全的控制中，
     * 比较常用的是ReentrantLock，可以显式加锁、释放锁。
     */
    private final ReentrantLock lock;
    private ConcurrentLinkedQueue<Integer> tickets;
    private boolean flag = true;
    public ReentrantLockTest2(){
        tickets = new ConcurrentLinkedQueue<>();
        lock = new ReentrantLock();
    }

    public void addTicket(int ticket) {
        tickets.offer(ticket);
    }

    @Override
    public void run() {
        while (flag) {
            buyTickets();
        }
    }

    public void buyTickets() {
        try {
            lock.lock();
            if (tickets.isEmpty()) {
                flag = false;
                return;
            }
            Integer ticketNo = tickets.peek();
            tickets.poll();
            System.out.println(Thread.currentThread().getName() + " 获得一张票编号为: "+ticketNo+"，还剩余"+tickets.size()+ "张票");

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockTest2 lockTest = new ReentrantLockTest2();
        for (int i = 1; i < 301; i++) {
            lockTest.addTicket(i);
        }
        new Thread(lockTest, "Tread1").start();
        new Thread(lockTest, "Tread2").start();
        new Thread(lockTest, "Tread3").start();
    }
}