package patterns.creation.factory.factorymethod.pizzastore.order;

import patterns.creation.factory.factorymethod.pizzastore.pizza.BJCheesePizza;
import patterns.creation.factory.factorymethod.pizzastore.pizza.BJPepperPizza;
import patterns.creation.factory.factorymethod.pizzastore.pizza.LDCheesePizza;
import patterns.creation.factory.factorymethod.pizzastore.pizza.LDPepperPizza;
import patterns.creation.factory.factorymethod.pizzastore.pizza.Pizza;


public class LDOrderPizza extends OrderPizza {

	
	@Override
	Pizza createPizza(String orderType) {
	
		Pizza pizza = null;
		if(orderType.equals("cheese")) {
			pizza = new LDCheesePizza();
		} else if (orderType.equals("pepper")) {
			pizza = new LDPepperPizza();
		}
		// TODO Auto-generated method stub
		return pizza;
	}

}
