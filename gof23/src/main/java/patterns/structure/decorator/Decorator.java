package patterns.structure.decorator;

/**
 * 装饰者模式定义 :
 * 1）装饰者模式：动态的将新功能(装饰者)附加到对象（主体）上。在对象功能扩展方面，它比维承更有弹性，装饰者模式也体现了开闭原则（ocp）
 * 2）这里提到的动态的将新功能附加到对象和ocp原则. 
 * 
 * 装饰者模式实现思路：
 * 1）首先区分谁是装饰者（牛奶，豆浆，巧克力），谁是主体（咖啡）。 
 * 2）然后将主体抽象类和实现类分开，装饰者抽象类和实现类分开；
 * 3）装饰者抽象类要继承主体抽象类，且主体抽象类要被聚合到装饰者抽象类中
 */
public class Decorator extends Drink {
	private Drink coffe;

	public Decorator(Drink coffe) { // 组合
		this.coffe = coffe;
	}

	@Override
	public float cost() {
		// getPrice 自己价格
		return super.getPrice() + coffe.cost();
	}

	@Override
	public String getName() {
		// 装饰者的名称+咖啡的名称
		return super.getName() + " & " + coffe.getName();
	}

}
