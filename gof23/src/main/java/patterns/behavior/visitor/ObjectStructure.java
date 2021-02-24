package patterns.behavior.visitor;

import java.util.LinkedList;
import java.util.List;

import patterns.behavior.visitor.action.Action;
import patterns.behavior.visitor.jury.Jury;

//数据结构，管理很多人（Expert , Audience）
public class ObjectStructure {

	//维护了一个集合
	private List<Jury> jurys = new LinkedList<>();
	
	//增加到list
	public void add(Jury jury) {
		jurys.add(jury);
	}
	//移除
	public void remove(Jury jury) {
		jurys.remove(jury);
	}
	
	//显示测评情况
	public void display(Action action) {
		for(Jury jury: jurys) {
			jury.vote(action);
		}
	}
}
