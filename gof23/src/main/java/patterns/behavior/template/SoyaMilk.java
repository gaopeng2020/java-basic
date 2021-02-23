package patterns.behavior.template;
/**
 * 基本介绍
 * 1）模板方法模式（Template Method Pattern），又叫模板模式（Template Pattern），
 * 在一个抽象类公开定义了执行它的方法的模板。它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行。
 * 2）简单说，模板方法模式定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，
 * 使得子类可以不改变一个算法的结构，就可以重定义该算法的某些特定步骤
 * 3）这种类型的设计模式属于行为型模式。
 */
//抽象类，表示豆浆
public abstract class SoyaMilk {

	//模板方法, make , 模板方法可以做成final , 不让子类去覆盖.
	final void make() {
		
		select(); 
		addCondiments();
		soak();
		beat();
		
	}
	
	//选材料
	void select() {
		System.out.println("第一步：选择好的新鲜黄豆  ");
	}
	
	//添加不同的配料， 抽象方法, 子类具体实现
	abstract void addCondiments();
	
	//浸泡
	void soak() {
		System.out.println("第三步， 黄豆和配料开始浸泡， 需要3小时 ");
	}
	 
	void beat() {
		System.out.println("第四步：黄豆和配料放到豆浆机去打碎  ");
	}
}
