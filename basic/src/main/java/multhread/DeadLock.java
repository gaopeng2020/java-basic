package multhread;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 21:41
 * @description：
 * @modified By：
 * @version: $
 */
public class DeadLock implements Runnable {
    /**
     * 产生死锁的四个必要条件：
     * 1·互斥条件：一个资源每次只能被一个进程使用。
     * 2.请求与保持条件：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
     * 3.不剥夺条件：进程已获得的资源，在末使用完之前，不能强行剥夺。
     * 4，循环等待条件：若干进程之间形成一种头尾相接的循环等待资源关系。
     * 上面列出了死锁的四个必要条件，我们只要想办法破其中的任意一个或多个条件就可以避免死锁发生
     */

    //飞机和火车玩具只有一个，用static修饰
    private static TrainToy trainToy = new TrainToy();
    private static AirPlane airPlane = new AirPlane();
    private String name;
    private CHOISE choise;

    @Override
    public void run() {
//        SnatchToy_DeadLock(name, choise);
        SnatchToy(name, choise);
    }

    public DeadLock(String name, CHOISE choise) {
        this.name = name;
        this.choise = choise;
    }

    public void SnatchToy(String name, CHOISE choise) {
        if (choise == CHOISE.AirPlane) {
            //先抢飞机玩具
            synchronized (airPlane) {
                System.out.println("恭喜[" + name + "]抢到飞机玩具");
                System.out.println(airPlane.toString());
            }
            //抢完飞机还想抢火车玩具,但此时trainToy已经被另外一个线程（小华）锁住了，只能等待小华解锁才能进入
            synchronized (trainToy) {
                System.out.println("恭喜[" + name + "]抢到火车玩具");
                System.out.println(trainToy.toString());
            }

        } else {
            synchronized (trainToy) {
                System.out.println("恭喜[" + name + "]抢到火车玩具");
                System.out.println(trainToy.toString());
            }
            synchronized (airPlane) {
                System.out.println("恭喜[" + name + "]抢到飞机玩具");
                System.out.println(airPlane.toString());
            }
        }
    }

    public void SnatchToy_DeadLock(String name, CHOISE choise) {
        if (choise == CHOISE.AirPlane) {
            //先抢飞机玩具
            synchronized (airPlane) {
                System.out.println("恭喜[" + name + "]抢到飞机玩具");
                System.out.println(airPlane.toString());
                //抢完飞机还想抢火车玩具
                synchronized (trainToy) {
                    System.out.println("恭喜[" + name + "]抢到火车玩具");
                    System.out.println(trainToy.toString());
                }
            }

        } else {
            synchronized (trainToy) {
                System.out.println("恭喜[" + name + "]抢到火车玩具");
                System.out.println(trainToy.toString());
                synchronized (airPlane) {
                    System.out.println("恭喜[" + name + "]抢到飞机玩具");
                    System.out.println(airPlane.toString());
                }
            }
        }

    }


    static class TrainToy {
        private String toyName = "飞机玩具";

        @Override
        public String toString() {
            return "TrainToy{" +
                    "toyName='" + toyName + '\'' +
                    '}';
        }
    }

    static class AirPlane {
        private String toyName = "火车玩具";

        @Override
        public String toString() {
            return "AirPlane{" +
                    "toyName='" + toyName + '\'' +
                    '}';
        }
    }

    enum CHOISE {
        TrainToy,
        AirPlane
    }

    public static void main(String[] args) {
        new Thread(new DeadLock("小明", CHOISE.AirPlane),"xiaoming").start();
        new Thread(new DeadLock("小华", CHOISE.TrainToy),"xiaohua").start();
    }
}
