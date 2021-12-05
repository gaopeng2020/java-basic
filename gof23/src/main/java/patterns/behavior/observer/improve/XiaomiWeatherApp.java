package patterns.behavior.observer.improve;

/**
 * @author Administrator
 */
public class XiaomiWeatherApp implements Observer {

	/**
	 * 温度，气压，湿度
	 */
	private float temperature;
	private float pressure;
	private float humidity;

	/**
	 * 更新 天气情况，是由 WeatherData 来调用，我使用推送模式
	 * @param temperature temperature
	 * @param pressure pressure
	 * @param humidity humidity
	 */
	@Override
	public void update(float temperature, float pressure, float humidity) {
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidity = humidity;
		display();
	}

	// 显示
	public void display() {
		System.out.println("***ChinaWeather Temperature: " + temperature + "***");
		System.out.println("***ChinaWeather Pressure: " + pressure + "***");
		System.out.println("***ChinaWeather Humidity: " + humidity + "***");
	}
}
