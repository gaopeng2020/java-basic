package patterns.structure.decorator;

/**
 * Drink类代表的是主体(咖啡)和装饰者(牛奶等附加物)的混合饮品
 *
 */
public abstract class Drink {

	private String name; // 描述
	private float price = 0.0f;

	public String getName() {
		return name;
	}

	public void setName(String des) {
		this.name = des;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	// 计算费用的抽象方法
	// 子类来实现
	public abstract float cost();

}
