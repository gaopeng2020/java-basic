package basic.collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MapKeysetEntrySet {
public static void main(String[] args) {
	Map<String,Integer> map = new LinkedHashMap<String, Integer>();
	map.put("小明", 20);
	map.put("小红", 21);
	map.put("小花", 22);
	map.put("小崔", 23);
	map.put("小雷", 23);
	map.put("小雷", 25);
	Mapkeyset(map);
	MapEntryset(map);

}


private static void Mapkeyset(Map<String,Integer> map) {
	System.out.println("=======Iterator用于keyset========");	
	//1. 调用map集合的方法keySet,把Map集合中所有的key存储到Set集合
	Set<String> set = map.keySet();
	//2. 使用iterator遍历集合,获取集合的key
	Iterator<String> it = set.iterator();
	while(it.hasNext()) {
		String key = it.next();
	//3. 调用map集合方法get,通过key获取value
		Integer value = map.get(key);
		System.out.println(key+"==="+value);
		
	}
	System.out.println("=======增强for循环用于keyset=======");
	//使用增强for循环，用key遍历集合
	for(String key : map.keySet()) {
		//调用map集合方法get,通过key获取value
		System.out.println(key+"----"+map.get(key));
	}
	
}

private static void MapEntryset(Map<String, Integer> map) {
	System.out.println("=======Iterator用于entryset=======");
	//1.创建set集合,调用map集合方法entrySet()将map集合封装成键值对对象。
	//注意：Entry是Map接口的内部接口，内部类的调用方式：外部类.内部类<>
	Set<Map.Entry <String,Integer> >  set = map.entrySet();
	//2. 迭代Set集合，遍历set集合中的键值对对象，it代表键值对对象
	Iterator<Map.Entry <String,Integer> > it = set.iterator();
	while(it.hasNext()){
		//定义一个entry集合存储it,即存储键值对对象
		Map.Entry <String,Integer> entry =it.next();
		//调用entry方法的getKey()，getValue()
		String key = entry.getKey();
		Integer value = entry.getValue();
		System.out.println(key+"###"+value);
	}
	System.out.println("======增强for循环用于entryset=======");
	//使用增强for循环
	for(Map.Entry<String, Integer> entry : map.entrySet()) {
		System.out.println(entry.getKey()+"@@@"+entry.getValue());
	}
}
}

