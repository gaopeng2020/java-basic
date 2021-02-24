package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Action;

//说明
//1. 这里我们使用到了双分派, 即首先在客户端程序中，将具体状态作为参数传递Woman中(第一次分派)
//2. 然后Woman 类调用作为参数的 "具体方法" 中方法getWomanResult, 同时将自己(this)作为参数
//   传入，完成第二次的分派
public class Expert extends Jury {
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
	public void vote(Action action) {
		action.getExpertResult(this);
	}

}
