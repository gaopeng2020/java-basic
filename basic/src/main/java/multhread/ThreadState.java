package multhread;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 21:04
 * @description：Thread State Test
 * @modified By：
 * @version: $
 */
public class ThreadState {
    /**
     * 线程状态：
     * NEW 尚未启动的线湿处于此状态。
     * RUNNABLE 执行线程处于此状态。
     * BLOCKED 等待监视器锁定的线程处于吃状态。
     * WAITING 正在等待另一个线程执行特定作的线程处于此状态。
     * TIMED WAITING 正在等待另一个线程执行动作达到指定等待时间的线程处于此状态。
     * TERMINATED 退出的线程处于此状态。
     */
    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("============================");
        });

        //线程启动之前的状态
        System.out.println("=============线程启动之前的状态===============");
        Thread.State state_NEW = thread.getState();
        System.out.println("state = " +state_NEW);

        //线程启动之后的状态
        thread.start();
        System.out.println("=============线程启动后的状态===============");
        Thread.State state_RUNNALE = thread.getState();
        System.out.println("state = " +state_RUNNALE);

        //线程等待的状态
        System.out.println("=============线程运行中的状态===============");
        while (thread.getState() != Thread.State.TERMINATED) {
            Thread.State state_TIMED_WAITING = thread.getState();
            System.out.println("state = " + state_TIMED_WAITING);
        }

        System.out.println("=============线程结束后的状态===============");
        System.out.println("state = " +thread.getState());
    }

}

