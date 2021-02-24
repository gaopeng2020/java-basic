package patterns.behavior.visitor.action;

import patterns.behavior.visitor.jury.Audience;
import patterns.behavior.visitor.jury.Expert;

public abstract class Action {

	// 得到男性 的测评
	public abstract void getAudienceResult(Audience audience);

	// 得到女的 测评
	public abstract void getExpertResult(Expert expert);
}
