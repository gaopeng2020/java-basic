package patterns.creation.factory.absfactory.pizzastore.order;


import patterns.creation.factory.absfactory.pizzastore.pizza.BJCheesePizza;
import patterns.creation.factory.absfactory.pizzastore.pizza.BJPepperPizza;
import patterns.creation.factory.absfactory.pizzastore.pizza.Pizza;

//这是工厂子类
public class BJFactory implements AbsFactory {

	@Override
	public Pizza createPizza(String orderType) {
		System.out.println("~使用的是抽象工厂模式~");
		Pizza pizza = null;
		if(orderType.equals("cheese")) {
			pizza = new BJCheesePizza();
		} else if (orderType.equals("pepper")){
			pizza = new BJPepperPizza();
		}
		return pizza;
	}

}
