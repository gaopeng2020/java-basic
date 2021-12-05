package patterns.structure.proxy.staticproxy;


/**
 * 代理对象,静态代理
 * @author Administrator
 */
public class TeacherDaoProxy implements ITeacherDao{
	/**
	 * 目标对象，通过接口来聚合
	 */
	private final ITeacherDao target;
	
	
	/**
	 * 构造器
	 * @param target target
	 */
	public TeacherDaoProxy(ITeacherDao target) {
		this.target = target;
	}

	@Override
	public void teach() {
		System.out.println("开始代理  完成某些操作。。。。。 ");
		target.teach();
		System.out.println("提交。。。。。");
	}

}
