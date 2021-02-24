package patterns.behavior.visitor.jury;

import patterns.behavior.visitor.action.Action;

public class Audience extends Jury {
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
	public void vote(Action action) {
		action.getAudienceResult(this);
	}

}
