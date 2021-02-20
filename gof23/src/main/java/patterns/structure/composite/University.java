package patterns.structure.composite;

import java.util.ArrayList;
import java.util.List;

//University 也是 Composite , 可以管理College
public class University extends Organization {

	List<Organization> colleges = new ArrayList<Organization>();

	// 构造器
	public University(String name, String des) {
		super(name, des);
	}

	// 重写add
	@Override
	protected void add(Organization organization) {
		colleges.add(organization);
	}

	// 重写remove
	@Override
	protected void remove(Organization organization) {
		colleges.remove(organization);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public String getDes() {
		return super.getDes();
	}

	// print方法，就是输出University 包含的学院
	@Override
	protected void print() {
		System.out.println("--------------" + getName() + "--------------");
		// 遍历 organization
		for (Organization organizationComponent : colleges) {
			organizationComponent.print();
		}
	}

}
