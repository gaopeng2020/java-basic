package basic.oop.grammer;

public class keyBoard implements USB {
	@Override
	public void USBon() {
		System.out.println("KeyBoard is PowerOn");
	}
	@Override
	public void USBoff() {
		System.out.println("KeyBoard is PowerOff");
	}
}
