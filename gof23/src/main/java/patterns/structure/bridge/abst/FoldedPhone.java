package patterns.structure.bridge.abst;

import patterns.structure.bridge.impl.Phone;

//折叠式手机类，继承 抽象类 Phone
public class FoldedPhone extends Bridge {

	//构造器
	public FoldedPhone(Phone phone) {
		super(phone);
		System.out.println("你现在创建的是折叠式手机");
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
