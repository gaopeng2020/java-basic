package patterns.structure.bridge.abst;

import patterns.structure.bridge.impl.Phone;

/**
 * 接口适配器模式介绍
 * 1）一些书籍称为：适配器模式（Default Adapter Pattern）或缺省适配器模式。
 * 2）当不需要全部实现接口提供的方法时，可先设计一个抽象类实现接口，并为该接口中每个方法提供一个默认实现（空方法），那么该抽象类的子类可有选择地覆盖父类的某些方法来实现需求
 * 3）适用于一个接口不想使用其所有的方法的情况。
 */
/**
 * 桥接模式的注意事项和细节
 * 1）实现了抽象和实现部分的分离，从而极大的提供了系统的灵活性，让抽象部分和实现部分独立开来，这有助于系统进行分层设计，从而产生更好的结构化系统。
 * 2）对于系统的高层部分，只需要知道抽象部分和实现部分的接口就可以了，其它的部分由具体业务来完成。
 * 3）桥接模式替代多层继承方案，可以减少子类的个数，降低系统的管理和维护成本。
 * 4）桥接模式的引入增加了系统的理解和设计难度，由于聚合关联关系建立在抽象层，要求开发者针对抽象进行设计和编程
 * 5）桥接模式要求正确识别出系统中两个独立变化的维度，因此其使用范围有一定的局限性，即需要有这样的应用场景。
 *
 */

/**
 * 桥接模式其它应用场景 
 * 1）对于那些不希望使用继承或因为多层次继承导致系统类的个数急剧增加的系统，桥接模式无为适用. 
 * 2）常见的应用场景：
 * -JDBC驱动程序 
 * -银行特账系统
	 * 转账分类： * 网上转账，柜台转账，AMT转账转账
	 * 用户类型：普通用户，银卡用户，金卡用户.. 
* -消息管理
	 * 消息类型： 即时消息，延时消息 
	 * 消息分类：手机短信，邮件消息，QQ消息
 */

public abstract class Bridge {

	// 组合品牌
	private Phone phone;

	// 构造器
	public Bridge(Phone phone) {
		super();
		this.phone = phone;
	}

	protected void open() {
		this.phone.open();
	}

	protected void close() {
		phone.close();
	}

	protected void call() {
		phone.call();
	}

}
