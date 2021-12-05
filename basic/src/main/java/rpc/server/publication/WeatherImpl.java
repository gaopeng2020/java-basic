package rpc.server.publication;

import java.util.ArrayList;

/**
 * 类是核心
 * 1. 包含最新的天气情况信息 
 * 2. 含有 观察者集合，使用ArrayList管理
 * 3. 当数据有更新时，就主动的调用   ArrayList, 通知所有的（接入方）就看到最新的信息
 * @author Administrator
 *
 */
public class WeatherImpl implements WeatherSubject {
	private float temperature;
	private float pressure;
	private float humidity;
	/**
	 *
	 */
	private final ArrayList<Observer> observers;
	
	//加入新的第三方

	public WeatherImpl() {
		observers = new ArrayList<Observer>();
	}

	public float getTemperature() {
		return temperature;
	}

	public float getPressure() {
		return pressure;
	}

	public float getHumidity() {
		return humidity;
	}


	/**
	 * 数据有更新时，就调用 setData
	 * @param temperature temperature（℃）
	 * @param pressure pressure
	 * @param humidity humidity
	 */
	public void weatherDataUpdate(float temperature, float pressure, float humidity) {
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidity = humidity;
		//notifyObservers， 将最新的信息 推送给 接入方 currentConditions
		notifyObservers();
	}

	/**
	 * 注册观察者
	 * @param o 观察者（订阅者）
	 */
	@Override
	public String registerObserver(Observer o) {
		observers.add(o);
//		weatherDataUpdate(0.0f, 0.0f, 0.0f);
		return "Subscribe successfully";
	}


	/**
	 * 移除一个观察者
	 * @param o 观察者（订阅者）
	 */
	@Override
	public String removeObserver(Observer o) {
		observers.remove(o);
		return "Unsubscribed successfully";
	}

	/**
	 * 遍历所有的观察者，并通知
	 */
	private void notifyObservers() {
		for (Observer observer : observers) {
			observer.updateNotify(this.temperature, this.pressure, this.humidity);
		}
	}
}
