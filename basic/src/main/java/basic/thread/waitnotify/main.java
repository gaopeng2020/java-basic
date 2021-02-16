package basic.thread.waitnotify;

public class main {

	public static void main(String[] args) {
		Resource rs = new Resource();
		input in = new input(rs);
		Outpute out = new Outpute(rs);
		Thread t1 = new Thread(in);
		Thread t2 = new Thread(out);
		t1.start();
		t2.start();
	}

}
