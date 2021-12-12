package patterns.creation.factory.absfactory.pizzastore.order;

import patterns.creation.factory.absfactory.pizzastore.order.AbsFactory;
import patterns.creation.factory.absfactory.pizzastore.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class OrderPizza {

	AbsFactory factory;

	// 构造器
	public OrderPizza(AbsFactory factory) {
		this.factory = factory;
		setFactory();
	}

	private void setFactory() {
		Pizza pizza = null;
		String orderType = ""; // 用户输入
		do {
			orderType = getType();
			// factory 可能是北京的工厂子类，也可能是伦敦的工厂子类
			pizza = factory.createPizza(orderType);
			if (pizza != null) { // 订购ok
				pizza.prepare();
				pizza.bake();
				pizza.cut();
				pizza.box();
			} else {
				System.out.println("订购失败");
				break;
			}
		} while (true);
	}

	// 写一个方法，可以获取客户希望订购的披萨种类
	private String getType() {
		try {
			BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("input pizza 种类:");
			return strin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
