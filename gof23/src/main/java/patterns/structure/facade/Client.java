package patterns.structure.facade;

/**
 * 项目需求：
 * 组建一个家庭影院：DVD播放器、投影仪、自动屏幕、环绕立体声、爆米花机，要求完成使用家庭影院的功能，其过程为：
 * 直接用递控器：统筹各设备开关
 * 开爆米花机
 * 放下下屏幕
 * 开設影仪
 * 开音响
 * 开DVD，透dd去章選米花
 * 调增灯光
 * ·編放
 * ·观影结束后，关闭各种设背
 */
public class Client {

	public static void main(String[] args) {
		TheaterFacade homeTheaterFacade = new TheaterFacade();
		homeTheaterFacade.ready();
		homeTheaterFacade.play();
		homeTheaterFacade.end();
	}

}
