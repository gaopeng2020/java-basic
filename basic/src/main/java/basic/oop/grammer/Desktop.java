package basic.oop.grammer;

public class Desktop {
	public void DesktopOn() {
		System.out.println("Desktop is starting");
	};
	
	public void UseUSB(USB usb) {
		usb.USBon();
		usb.USBoff();
	};
	
	public void DesktopOff() {
		System.out.println("Desktop is Shutting down");
	};
}
