package patterns.structure.adapter.objectadapter;

/**
 * 对象适配器模式介绍
	1）基本思路和类的适配器模式相同，只是Adapter类做修改，不是继承src类，
	     而是持有SrC类的实例，以解决兼容性的问。即，持有src类，实现gst类接口，完成Src->dst的适配
	2）根据“合成复用原则”，在系统中尽量使用关联关系来替代继承关系。
	3）对象适配器模式是适配器模式常用的一种
 *
 *对象适配器模式注意事项和细节
	1）对象适配器和类适配器其实算是同一种思想，只不过实现方式不同。
	根据合成复用原则，使用组合替代继承，所以它解决了类适配器必须继承src的局限性问题，也不再要求dst必须是接口。
	2）使用成本更低，更灵活。
 */


//适配器类
public class VoltageAdapter  implements IVoltage5V {

	private Voltage220V voltage220V; // 关联关系-聚合
	
	
	//通过构造器，传入一个 Voltage220V 实例
	public VoltageAdapter(Voltage220V voltage220v) {
		
		this.voltage220V = voltage220v;
	}



	@Override
	public int output5V() {
		
		int dst = 0;
		if(null != voltage220V) {
			int src = voltage220V.output220V();//获取220V 电压
			System.out.println("使用对象适配器，进行适配~~");
			dst = src / 44;
			System.out.println("适配完成，输出的电压为=" + dst);
		}
		
		return dst;
		
	}

}
