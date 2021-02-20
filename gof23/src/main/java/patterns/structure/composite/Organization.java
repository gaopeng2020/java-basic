package patterns.structure.composite;

/**
 * 组合模式基本介绍
基本介绍
1）组合模式（Composite Pattern），又叫部分整体模式，它创建了对象组的树形结构，将对象组合成树状结构以表示“整体-部分”的层次关系。
2）组合模式依据树形结构来组合对象，用来表示部分以及整体层次。
3）这种类型的设计模式属于结构型模式。
4）组合模式使得用户对单个对象和组合对象的访问具有一致性，即：组合能让客户以一致的方式处理个别对象以及组合对象
5）在组合模式中所有被管理的节点（Leaf与Composite节点）都要继承一个有相同抽象属性的Component抽象类。
 *
 *组合模式解决的问题：当我们的要处理的对象可以生成一颗树形结构，而我们要对树上的节点和叶子进行操作时，
 *它能够提供一致的方式，而不用考虑它是节点还是叶子
 */
public abstract class Organization {

	private String name; // 名字
	private String des; // 说明

	protected void add(Organization organizationComponent) {
		// 默认实现
		throw new UnsupportedOperationException();
	}

	protected void remove(Organization organizationComponent) {
		// 默认实现
		throw new UnsupportedOperationException();
	}

	// 构造器
	public Organization(String name, String des) {
		super();
		this.name = name;
		this.des = des;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	// 方法print, 做成抽象的, 子类都需要实现
	protected abstract void print();

}
