package patterns.creation.factory.absfactory.pizzastore.order;

public class PizzaStoreClient {

	public static void main(String[] args) {
		//new OrderPizza(new BJFactory());
		new OrderPizza(new LDFactory());
	}

}
