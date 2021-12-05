package patterns.behavior.observer.improve;


/**
 * 接口, 让WeatherData 来实现
 */
public interface Subject {
	/**
	 * 注册观察值（订阅者）
	 * @param o 观察者（订阅者）
	 */
	public void registerObserver(Observer o);

	/**
	 * 取消注册观察值（订阅者）
	 * @param o 观察者（订阅者）
	 */
	public void removeObserver(Observer o);

	/**
	 * 通知观察值（订阅者）
	 */
	public void notifyObservers();
}
