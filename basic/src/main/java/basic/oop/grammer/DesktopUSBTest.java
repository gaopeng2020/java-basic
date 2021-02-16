package basic.oop.grammer;

public class DesktopUSBTest {

	public static void main(String[] args) {
		Desktop dell = new Desktop();
		USB usb1= new Mouse();
		USB usb2 = new keyBoard();
		dell.DesktopOn();
		dell.UseUSB(usb1);
		dell.UseUSB(usb2);
		dell.DesktopOff();
	}

}
