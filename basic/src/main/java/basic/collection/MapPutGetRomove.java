package basic.collection;

import java.util.HashMap;
import java.util.Map;

public class MapPutGetRomove {
	public static void main(String[] args) {
		function();
	}


	public static void function(){
		//创建map对象，存储集合
		Map<Integer,String> map = new HashMap<Integer, String>();
		//V put(K,V) K为map集合赋值键值对
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
		map.put(4, "d");
		map.put(3, "e");
		
		System.out.println(map);
		//V remove(K)方法返回移除制定的键值对，并返回移除键对应的值
		System.out.println(map.remove(3));
		//V get(K)指定key返回value，没有则返回null
		System.out.println(map.get(3));
		System.out.println(map);
	}
}
