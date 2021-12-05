package rpc.client.subscriber;

import rpc.server.publication.Observer;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class XiaomiWeatherApp implements Observer,Serializable {

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
	public void updateNotify(float temperature, float pressure, float humidity) {
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidity = humidity;
		displayWeatherInfo();
	}

	/**
	 * 显示天气信息
	 */
	public void displayWeatherInfo() {
		System.out.println("===小米手机天气APP====");
		System.out.println("***小米手机天显示的温度 : " + temperature + "***");
		System.out.println("***小米手机天显示的气压: " + pressure + "***");
		System.out.println("***小米手机天显示的湿度: " + humidity + "***");
	}
}
