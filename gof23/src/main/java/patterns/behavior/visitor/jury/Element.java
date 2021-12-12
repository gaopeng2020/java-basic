package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Visitor;

public abstract class Element {
	
	//提供一个方法，让访问者可以访问
	public abstract void accept(Visitor visitor); //第一次多态辨析
}
