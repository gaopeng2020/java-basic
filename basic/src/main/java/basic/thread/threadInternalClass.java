package basic.thread;

public class threadInternalClass {

	public static void main(String[] args) {
		ResourceI rs = new ResourceI();
		inputI in = new inputI(rs);
		OutputeI out = new OutputeI(rs);
		Thread t1 = new Thread(in);
		Thread t2 = new Thread(out);
		t1.start();
		t2.start();
	}

}

/*
 * 线程同步的方式有两种：  方式1：同步代码块(同步代码块中的锁对象可以是任意的对象；但多个线程时，要使用同一个锁对象才能够保证线程安全)
 * synchronized (锁对象) { 可能会产生线程安全问题的代码 }
 * 
 *  方式2：同步方法: 使用synchronized修饰符
 * 
 */

class ResourceI {
	private String name;
	private String sex;
	boolean flag = false;

	public synchronized void setContent(String name, String sex) {
		if (flag) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			this.name = name;
			this.sex = sex;
			flag = true;
			this.notify();
		}
	}

	public synchronized void getContent() {
		if (!flag) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(this.name + "   " + this.sex);
			flag = false;
			this.notify();
		}
	}

}

class inputI implements Runnable {
	private ResourceI rs;

	public inputI(ResourceI rs) {
		this.rs = rs;
	}

	@Override
	public void run() {
		int count = 0;
		while (true) {
			if (count % 2 == 0) {
				rs.setContent("小明", "男生" + " : " + count);
			} else {
				rs.setContent("xiaohua", "girl" + " : " + count);
			}
			// 在两个数据之间进行切换
			count++;
		}

	}

}

class OutputeI implements Runnable {

	private ResourceI rs;

	public OutputeI(ResourceI rs) {
		this.rs = rs;
	}

	@Override
	public void run() {
		while (true) {
			rs.getContent();
		}
	}
}