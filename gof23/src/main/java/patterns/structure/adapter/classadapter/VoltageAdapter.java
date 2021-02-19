package patterns.structure.adapter.classadapter;
/**
 *  适配器基本介绍
 * 1）适配器模式（Adapter Pattern）将某个类的接口转换成客户端期望的另一个接口表示，
 * 	主的目的是兼容性，让原本因接口不匹配不能一起工作的两个类可以协同工作。其别名为包装器（Wrapper）
 * 2）适配器模式属于结构型模式
 * 3）主要分为三类：类适配器模式、对象适配器模式、接口适配器模式
 */

//适配器类
/**
  * 类适配器模式注意事项和细节
 *	1）Java是单继承机制，所以类适配器需要继承src类这一点算是一个缺点，因为这要求dst必须是接口，有一定局限性；
 *	2）src类的方法在Adapter中都会暴露出来，也增加了使用的成本。
 *	3）由于其继承了src类，所以它可以限据需求重写src类的方法，使得Adapter的灵活性增强了。
 */
public class VoltageAdapter extends Voltage220V implements IVoltage5V {

	@Override
	public int output5V() {
		// TODO Auto-generated method stub
		//获取到220V电压
		int srcV = output220V();
		int dstV = srcV / 44 ; //转成 5v
		return dstV;
	}

}
