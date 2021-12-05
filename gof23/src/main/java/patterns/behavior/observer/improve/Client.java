package patterns.behavior.observer.improve;

public class Client {

	public static void main(String[] args) {
		//创建一个WeatherData
		WeatherData weatherData = new WeatherData();
		
		//创建观察者
		XiaomiWeatherApp xiaomiWeatherApp = new XiaomiWeatherApp();
		HuaweiWeatherApp huaweiWeatherApp = new HuaweiWeatherApp();
		
		//注册到weatherData
		weatherData.registerObserver(xiaomiWeatherApp);
		weatherData.registerObserver(huaweiWeatherApp);
		
		//测试
		System.out.println("通知各个注册的观察者, 看看信息");
		weatherData.setData(10f, 100f, 30.3f);
		
		
		weatherData.removeObserver(xiaomiWeatherApp);
		//测试
		System.out.println();
		System.out.println("通知各个注册的观察者, 看看信息");
		weatherData.setData(10f, 100f, 30.3f);
	}

}
