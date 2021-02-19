package patterns.structure.decorator;

import patterns.structure.decorator.accessories.Chocolate;
import patterns.structure.decorator.accessories.Milk;
import patterns.structure.decorator.coffee.DeCaf;
import patterns.structure.decorator.coffee.LongBlack;

/**
 * 星巴克咖啡订单项目（咖啡馆）：
 * 1）咖啡种类/单品咖啡：Espresso（意大利浓咖啡）、ShorBlack，LongBlack（美式咖啡）、Decaf（无因咖啡）
 * 2）调料：Milk.Soy（豆浆），Chocolate 
 * 3）要求在扩展新的咖啡种类时，具有良好的扩展性、改动方便、维护方便
 * 4）使用00的来计算不同种类咖啡的费用：客户可以点单品咖啡，也可以单品咖啡+调料组合。
 *
 */
public class CoffeeBarClient {

	// 装饰者模式下的订单：2份巧克力+一份牛奶的LongBlack
	public static void main(String[] args) {

		// 1. 点一份 LongBlack
		Drink longBlack = new LongBlack();
		System.out.println("LongBlack描述=" + longBlack.getName());
		System.out.println("LongBlack费用=" + longBlack.cost());

		// 2. order 加入一份牛奶
		longBlack = new Milk(longBlack);
		System.out.println("LongBlack 加入一份牛奶 描述 = " + longBlack.getName());
		System.out.println("LongBlack 加入一份牛奶 费用 =" + longBlack.cost());

		// 3. order 加入一份巧克力
		longBlack = new Chocolate(longBlack);
		System.out.println("LongBlack 加入一份牛奶 加入一份巧克力 描述 = " + longBlack.getName());
		System.out.println("LongBlack 加入一份牛奶 加入一份巧克力  费用 =" + longBlack.cost());

		// 4. order 加入一份巧克力
		longBlack = new Chocolate(longBlack);
		System.out.println("LongBlack 加入一份牛奶 加入一份巧克力 描述 = " + longBlack.getName());
		System.out.println("LongBlack 加入一份牛奶 加入一份巧克力   费用 =" + longBlack.cost());
	}

}
