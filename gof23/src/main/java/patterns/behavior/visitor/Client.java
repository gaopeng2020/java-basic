package patterns.behavior.visitor;

import patterns.behavior.visitor.action.Fail;
import patterns.behavior.visitor.action.Waiver;
import patterns.behavior.visitor.action.Success;
import patterns.behavior.visitor.jury.Audience;
import patterns.behavior.visitor.jury.Expert;

public class Client {

	public static void main(String[] args) {
		// 创建ObjectStructure
		ObjectStructure objectStructure = new ObjectStructure();

		objectStructure.attach(new Audience("张三"));
		objectStructure.attach(new Expert("李四"));

		// 成功
		Success success = new Success();
		objectStructure.display(success);

		System.out.println("===============");
		Fail fail = new Fail();
		objectStructure.display(fail);

		System.out.println("=======给的是待定的测评========");

		Waiver pending = new Waiver();
		objectStructure.display(pending);
	}

}
