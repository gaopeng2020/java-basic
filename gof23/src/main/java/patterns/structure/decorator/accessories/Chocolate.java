package patterns.structure.decorator.accessories;

import patterns.structure.decorator.Decorator;
import patterns.structure.decorator.Drink;

//具体的Decorator， 这里就是调味品
public class Chocolate extends Decorator {

	public Chocolate(Drink obj) {
		super(obj);
		super.setName(" 巧克力 ");
		super.setPrice(3.0f); // 调味品 的价格
	}

}
