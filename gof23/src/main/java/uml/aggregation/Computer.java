package uml.aggregation;

/**
 * 组合和聚合都以一种特殊的依赖关系，区别在于组合对象和被组合对象是否同生共死的关系；
 * 聚合：组合对象和被组合对象不是同生共死的关系；一般通过Setter窜入组合对象；
 * 组合：组合对象和被组合对象不是同生共死的关系，一把通过New的方法来构建组合对象。
 */
public class Computer {
	private Mouse mouse; //鼠标可以和computer分离
	private Moniter moniter;//显示器可以和Computer分离
	public void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}
	public void setMoniter(Moniter moniter) {
		this.moniter = moniter;
	}
	
}
