package patterns.behavior.template.improve;

public class PureSoyaMilk extends SoyaMilk{

	//纯豆浆不需要添加任何辅料，空实现即可
	@Override
	void addCondiments() {
		//空实现
	}

	//重新是否添加辅料的方法，返回false即可实现不添加辅料的功能
	@Override
	boolean customerWantCondiments() {
		return false;
	}
 
}
