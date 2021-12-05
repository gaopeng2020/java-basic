package rpc.server.publication;


/**
 * 接口, 让WeatherData 来实现
 * @author Administrator
 */
public interface WeatherSubject {
	/**
     * 注册观察值（订阅者）
	 * @param o 观察者（订阅者）
	 */
	public String registerObserver(Observer o);

    /**
     * 取消注册观察值（订阅者）
	 * @param o 观察者（订阅者）
	 */
	public String removeObserver(Observer o);

}
