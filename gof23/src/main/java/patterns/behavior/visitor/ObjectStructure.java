package patterns.behavior.visitor;

import java.util.LinkedList;
import java.util.List;

import patterns.behavior.visitor.action.Visitor;
import patterns.behavior.visitor.jury.Element;


//数据结构，管理很多人（Man , Woman）
public class ObjectStructure {

	//维护了一个集合
	private List<Element> list = new LinkedList<>();
	
	//增加到list
	public void add(Element jury) {
		list.add(jury);
	}
	//移除
	public void remove(Element jury) {
		list.remove(jury);
	}
	
	//显示测评情况
	public void display(Visitor action) {
		for(Element element : list) {
			element.accept(action);
		}
	}
}
