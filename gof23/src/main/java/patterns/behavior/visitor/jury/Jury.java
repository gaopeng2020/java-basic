package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Action;

public abstract class Jury {
	
	//提供一个方法，让访问者可以访问
	public abstract void vote(Action action);
}
