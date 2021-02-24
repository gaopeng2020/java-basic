package patterns.behavior.visitor;

import patterns.behavior.visitor.action.Action;
import patterns.behavior.visitor.jury.Jury;

import java.util.LinkedList;
import java.util.List;

/**
 * 访问者模式基本介绍
 * 1）访问者模式（Visitor Pattern），封装一些作用于某种数据结构的各元素的操作，它可以在不改变数据结构的前提下定义作用于这些元素的新的操作。
 * 2）主要将数据结构与数据操作分离，解决数据结构和操作耦合性问题3）访问者模式的基本工作原理是：在被访问的类里面加一个对外提供接待访问者的接口
 * 4）访问者模式主要应用场景是：需要对一个对象结构中的对象进行很多不同操作
 * （这些操作彼此没有关联），同时需要避免让这些操作"污染"这些对象的类，可以选用访问者模式解决
 */

/**
 * 访问者模式的注意事项和细节
 * >优点
 * 1）访问者模式符合单一职责原则、让程序具有优秀的扩展性、灵活性非常高2）访问者模式可以对功能进行统一，
 * 可以做报表、U1、拦截器与过滤器，适用于数据结构相对稳定的系统
 * >缺点
 * 1）具体元素对访问者公布细节，也就是说访问者关注了其他类的内部细节，这是迪米特法则所不建议的，这样造成了具体元素变更比较困难
 * 2）违背了依赖倒转原则。访问者依赖的是具体元素，而不是抽象元素
 * 3）因此，如果一个系统有比较稳定的数据结构，又有经常变化的功能需求，那么访问者模式就是比较合适的.
 */
//数据结构，管理很多人（Man , Woman）
public class ObjectStructure {

	//维护了一个集合
	private List<Jury> persons = new LinkedList<>();
	
	//增加到list
	public void attach(Jury jury) {
		persons.add(jury);
	}
	//移除
	public void detach(Jury jury) {
		persons.remove(jury);
	}
	
	//显示测评情况
	public void display(Action action) {
		for(Jury jury : persons) {
			jury.vote(action);
		}
	}
}
