package patterns.behavior.visitor.action;

import patterns.behavior.visitor.jury.Audience;
import patterns.behavior.visitor.jury.Expert;

public class Waiver extends Visitor {

	@Override
	public void getAudienceResult(Audience audience) {
		System.out.println("观众: "+audience.getName()+"弃权！");
	}

	@Override
	public void getExpertResult(Expert expert) {
		System.out.println("专家: "+expert.getName()+"弃权！");
	}

}
