package patterns.behavior.observer.improve;


/**
 * 观察者接口，有观察者来实现
 *
 * @author Administrator
 */
public interface Observer {
    /**
     * 更新天气信息
     *
     * @param temperature temperature（℃）
     * @param pressure    pressure
     * @param humidity    humidity
     */

    public void update(float temperature, float pressure, float humidity);
}
