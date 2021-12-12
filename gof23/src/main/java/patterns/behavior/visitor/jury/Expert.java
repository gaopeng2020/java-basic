package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Visitor;


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
