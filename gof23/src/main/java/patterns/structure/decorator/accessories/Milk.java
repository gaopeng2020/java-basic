package patterns.structure.decorator.accessories;

import patterns.structure.decorator.Decorator;
import patterns.structure.decorator.Drink;

public class Milk extends Decorator {

	public Milk(Drink obj) {
		super(obj);
		super.setName(" 牛奶 ");
		super.setPrice(2.0f); 
	}

}
