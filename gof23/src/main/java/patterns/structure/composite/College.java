package patterns.structure.composite;

import java.util.ArrayList;
import java.util.List;

//College 是 Composite , 可以管理major
public class College extends Organization {

	// List 中 存放的Major
	List<Organization> majors = new ArrayList<Organization>();

	// 构造器
	/**
	 * @param name
	 * @param des
	 */
	public College(String name, String des) {
		super(name, des);
	}

	// 重写add
	@Override
	protected void add(Organization organization) {
		majors.add(organization);
	}


	// 重写remove
	@Override
	protected void remove(Organization organization) {
		majors.remove(organization);
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
		// 遍历 organizations
		for (Organization organization : majors) {
			organization.print();
		}
	}

}
