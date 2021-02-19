package patterns.structure.decorator.accessories;

import patterns.structure.decorator.Decorator;
import patterns.structure.decorator.Drink;

public class Soy extends Decorator {

	public Soy(Drink obj) {
		super(obj);
		super.setName(" 豆浆  ");
		super.setPrice(1.5f);
	}

}
