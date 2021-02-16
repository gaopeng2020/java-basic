package basic.oop.grammer;

public class Mouse implements USB {
	@Override
	public void USBon(){
		System.out.println("Mouse is powerOn");
	}
	@Override
	public void USBoff(){
		System.out.println("Mouse is powerOff");
	}
}
