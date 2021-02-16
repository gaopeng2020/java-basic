package basic.thread.waitnotify;

public class input implements Runnable {
	private Resource rs;
	public input(Resource rs) {
		this.rs = rs;
	}
	@Override
	public void run() {
		int count = 0;
		while (true) {
			if (count% 2 == 0) {
				rs.setContent("小明", "男生");
			} else {
				rs.setContent("xiaohua", "girl");
			}
			// 在两个数据之间进行切换
			count++ ;
		}

	}

}
