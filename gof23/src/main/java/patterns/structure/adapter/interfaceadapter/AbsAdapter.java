package patterns.structure.adapter.interfaceadapter;

/**
 * 基本介绍
 * 1）适配器模式（Adapter Pattern）将某个类的接口转换成客户端期望的另一个接口表示，主的目的是兼容性，让原本因接口不匹配不能一起工作的两个类可以协同工作。其别名为包装器（Wrapper）
 * 2）适配器模式属于结构型模式
 * 3）主要分为三类：类适配器模式、对象适配器模式、接口适配器模式
 */
//在AbsAdapter 我们将 Interface4 的方法进行默认实现
public abstract class AbsAdapter implements Interface4 {

	//默认实现
	public void m1() {

	}

	public void m2() {

	}

	public void m3() {

	}

	public void m4() {

	}
}
