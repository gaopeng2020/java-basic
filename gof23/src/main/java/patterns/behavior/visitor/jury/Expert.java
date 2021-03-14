package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Visitor;

/*说明：
 * 访问者模式适用于ConcreteElement的数量是稳定的，但ConcreteElement的业务逻辑（方法）会发生变化的情况。
 *  访问者模式原理：将ConcreteElement类的业务逻辑（方法）放到ConcreteVisitor中实现，实现过程：
 *  1).在ConcreteVisitor中定义一个方法实现ConcreteElement类的业务逻辑（方法），因此需要将ConcreteVisitor作为参数传递给该方法。
 *  2).ConcreteElement需要定义一个方法accept()取接收ConcreteVisitor的处理结果，
 *  3).当ConcreteElement的业务逻辑需要变更时，通过扩展一个新的ConcreteVisitor即可实现。
 *  4).使用过程，new ConcreteElement( new ConcreteVisitor())
 *  
 *  visitor模式使用了Double Dispatch（双重多态辨析）
*/
public class Expert extends Element {
	private String name;

	public Expert(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.getExpertResult(this);  //第二次多态辨析
	}

}
