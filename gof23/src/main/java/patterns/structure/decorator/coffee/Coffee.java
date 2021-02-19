package patterns.structure.decorator.coffee;

import patterns.structure.decorator.Drink;

public class Coffee  extends Drink {

	@Override
	public float cost() {
		return super.getPrice();
	}

	
}
