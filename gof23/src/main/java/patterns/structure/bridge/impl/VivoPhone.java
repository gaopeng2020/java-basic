package patterns.structure.bridge.impl;

public class VivoPhone implements Phone {
	

	/**
	 * 
	 */
	public VivoPhone() {
		super();
	}

	@Override
	public void open() {
		System.out.println(" Vivo手机开机 ");
	}

	@Override
	public void close() {
		System.out.println(" Vivo手机关机 ");
	}

	@Override
	public void call() {
		System.out.println(" Vivo手机打电话 ");
	}

}
