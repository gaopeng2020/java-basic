package patterns.structure.bridge.abst;

import patterns.structure.bridge.impl.Phone;

public class UpRightPhone extends Bridge {
	
		//构造器
		public UpRightPhone(Phone phone) {
			super(phone);
			System.out.println("你现在创建的是直板式式手机");
		}
		
		public void open() {
			super.open();
		}
		
		public void close() {
			super.close();
		}
		
		public void call() {
			super.call();
		}
}
