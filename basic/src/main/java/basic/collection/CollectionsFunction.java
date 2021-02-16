package basic.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CollectionsFunction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Integer> list = new ArrayList<Integer>();
			list.add(1);
			list.add(3);
			list.add(9);
			list.add(7);
			list.add(6);
			list.add(10);
		ListColletions(list);
		
		Set<String> set = new LinkedHashSet<String>();
			set.add("小明");
			set.add("小红");
			set.add("小华");
			set.add("小嘻");
			set.add("小雷");
		SetCollections(set);
		
	}
	

	private static void ListColletions(List<Integer> list) {
		// Collections是集合工具，常用于排序、打乱排序
		Collections.sort(list, null);
		System.out.println(list);
		int index = Collections.binarySearch(list, 7);
		System.out.println(index);
		Collections.shuffle(list);
		System.out.println(list);
		
	}
	
	
	private static void SetCollections(Set<String> set) {
		// Set集合不能使用Sort、binarysearch、shuffle,如果想使用需要用遍历将其转化位List集合
		System.out.println(set);
		List<String> list = new ArrayList<String>();
		
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
		list.add(it.next());
		}
		Collections.sort(list, null);
		System.out.println(list);
		System.out.println(Collections.binarySearch(list, "小红"));
		Collections.shuffle(list);
		System.out.println(list);
		
		System.out.println(Collections.emptySet());
	}
	
}
