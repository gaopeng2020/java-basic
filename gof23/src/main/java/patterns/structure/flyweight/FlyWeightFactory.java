package patterns.structure.flyweight;

import java.util.HashMap;

/**
 * 享元模式基本介绍
基本介绍
1）享元模式（Flyweight Pattern）也叫蝇量模式：运用共享技术有效地支持大量细粒度的对象
2）常用于系统底层开发，解决系统的性能问题。像数据库连接池，里面都是创建好的连接对象，
    在这些连接对象中有我们需要的则直接拿来用，避免重新创建，如果没有我们需要的，则创建一个
3）享元模式能够解决重复对象的内存浪费的问题，当系统中有大量相似对象，需要缓冲池时。
    不需总是创建新对象，可以从缓冲池里拿。这样可以降低系统内存，同时提高效率
4）享元模式经典的应用场景就是池技术了，string常量池、数据库连接池、缓冲池等等都是享元模式的应用，享元模式是池技术的重要实现方式
 */

// 网站工厂类，根据需要返回压一个网站
public class FlyWeightFactory {

	// 集合， 充当池的作用
	private HashMap<String, ConcreteWebSite> pool = new HashMap<>();

	// 根据网站的类型，返回一个网站, 如果没有就创建一个网站，并放入到池中,并返回
	public WebSite getWebSiteCategory(String type) {
		if (!pool.containsKey(type)) {
			// 就创建一个网站，并放入到池中
			pool.put(type, new ConcreteWebSite(type));
		}

		return (WebSite) pool.get(type);
	}

	// 获取网站分类的总数 (池中有多少个网站类型)
	public int getWebSiteCount() {
		return pool.size();
	}
}
