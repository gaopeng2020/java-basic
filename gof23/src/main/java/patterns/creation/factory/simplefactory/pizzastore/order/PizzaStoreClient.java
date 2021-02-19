package patterns.creation.factory.simplefactory.pizzastore.order;

//相当于一个客户端，发出订购
public class PizzaStoreClient {

    public static void main(String[] args) {
        //使用简单工厂模式
        new OrderPizza(new SimpleFactory());
        System.out.println("~~退出程序~~");
    }
}
