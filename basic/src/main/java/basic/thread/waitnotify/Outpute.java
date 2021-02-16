package basic.thread.waitnotify;

public class Outpute implements Runnable {

	private Resource rs;
	public Outpute(Resource rs) {
		this.rs=rs;
	}
	@Override
	public void run() {
		while (true) {
			rs.getContent();
	}
	}
}
