package multhread;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 18:57
 * @description：runnable test
 * @modified By：
 * @version: 1.0$
 */
public class RunnableTest implements Runnable {
    /**
     * 定义一个类实现Runnable接口
     * 实现run()方法，编写线程执行体
     * 创建线程对象，调用start（）方法启动线程
     */
    private int ticketNum = 100;
    private boolean flag = true;

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
        RunnableTest runnableTest = new RunnableTest();
        Thread student = new Thread(runnableTest, "学生");
        student.setPriority(3);
        student.start();

        Thread teacher = new Thread(runnableTest, "教师");
        teacher.setPriority(5);
        teacher.start();

        Thread worker = new Thread(runnableTest, "工人");
        worker.setPriority(8);
        worker.start();

        Thread scalper = new Thread(runnableTest, "黄牛");
        scalper.setPriority(10);
        scalper.start();

    }
}
