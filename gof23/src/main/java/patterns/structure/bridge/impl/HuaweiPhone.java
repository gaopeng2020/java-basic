package patterns.structure.bridge.impl;

public class HuaweiPhone implements Phone {
	public HuaweiPhone() {
		super();
	}

	@Override
	public void open() {
		System.out.println(" 华为手机开机 ");
	}

	@Override
	public void close() {
		System.out.println(" 华为手机关机 ");
	}

	@Override
	public void call() {
		System.out.println(" 华为手机打电话 ");
	}

}
