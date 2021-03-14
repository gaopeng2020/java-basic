package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Visitor;

public class Audience extends Element {
	private  String name;
	public Audience(String name) {
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
		visitor.getAudienceResult(this);
	}

}
