package patterns.behavior.visitor.action;

import patterns.behavior.visitor.jury.Audience;
import patterns.behavior.visitor.jury.Expert;

public class Fail extends Visitor {

	@Override
	public void getAudienceResult(Audience audience) {
		System.out.println("观众: "+audience.getName()+"给的评价该歌手晋级失败 !");
	}

	@Override
	public void getExpertResult(Expert expert) {
		System.out.println("专家: "+expert.getName()+"给的评价该歌手晋级失败 !");
	}

}
