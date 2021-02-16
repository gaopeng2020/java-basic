package basic.collection;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 *  Map集合的嵌套,Map中存储的还是Map集合
 *  要求:按Map集合的嵌套Map集合的方式进行数据的存储
 *    1.班级中包含Java基础班和 Java就业班
 *    2.Java基础班
 *        001  张三
 *        002  李四
 *    3.Java就业班
 *        001  王五
 *        002  赵六
 *        
 *   Java基础班: 存储学号和姓名的键值对，如001 张三  键值对
 *   Java就业班: 类同
 *   班级: 存储的是Java基础班，与就业办
 */

public class CollectionNest {
	public static void main(String[] args) {
		//使用calssname作为for循环变量，用keyset方法得到班级集合
		System.out.println("#######For & keyset######");
		for(String classname: Class().keySet()) {
			//定义一个集合存储学生，调用map的get(classneme)方法得到学生集合
			Map<String,String> student = Class().get(classname);
			System.out.println(classname+"==========="+student);
			//使用学号作为for循环变量，继续调用map的get(num)即可得到学生姓名信息
			for(String num:student.keySet()) {
				System.out.println(classname+"###"+num+"###"+student.get(num));
				}
			}
		
		//使用Iterator遍历嵌套集合
		System.out.println("#######Iterator & Keyset######");
		Set<String> set = Class().keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String n = it.next();
			Map<String,String> student = Class().get(n);
			
			Set<String> set1 = student.keySet();
			Iterator<String> it1 = set1.iterator();
			while(it1.hasNext()) {
				String key = it1.next();
				String value = student.get(key);
				//System.out.println(j+k);
				System.out.println(n+key+value);
			}
		}
		
		//使用for循环以及entryset方法打印出所有的学生信息
		System.out.println("#######For & entryset######");
		for(Entry<String, Map<String, String>> mapClass : Class().entrySet()) {
			String clas =mapClass.getKey();
			Map<String,String> mapstudent = mapClass.getValue();
			for(Entry<String,String> students : mapstudent.entrySet()) {
				System.out.println(clas+"***"+students.getKey()+"***"+students.getValue());
			}
		}
		
		//使用Iterator遍历嵌套集合
		System.out.println("#######Iterator & entryset######");
		Set<Entry<String, Map<String, String>>> sete = Class().entrySet();
		Iterator<Entry<String, Map<String, String>>> ite = sete.iterator();
		while(ite.hasNext()) {
			Entry<String, Map<String, String>> entry =ite.next();
			String classkey = entry.getKey();
			Map<String, String> entry1= entry.getValue();
			
			Set<Entry<String,String>> sete1 =entry1.entrySet();
			Iterator<Entry<String,String>> ite1 = sete1.iterator();
			while(ite1.hasNext()) {
				Entry<String, String> entry2 = ite1.next();
				String num= entry2.getKey();
				String name = entry2.getValue();
				System.out.println(classkey+"%%%"+num+"%%%"+name);
			}
		}
	}
	
	
	//定义一个集合存储JavaSE班的学生信息
	private static Map<String, String> JavaSE() {
		// TODO Auto-generated method stub
		Map<String,String> mapjavaSE = new LinkedHashMap<String,String>();
		mapjavaSE.put("18101", "小花");
		mapjavaSE.put("18102", "小雷");
		mapjavaSE.put("18103", "小嘻");
		return mapjavaSE;
	}
	
	//定义一个集合存储JavaEE班的学生信息
	private static Map<String, String> JavaEE() {
		// TODO Auto-generated method stub
		Map<String,String> mapjavaEE = new LinkedHashMap<String,String>();
		mapjavaEE.put("18001", "小明");
		mapjavaEE.put("18002", "小红");
		mapjavaEE.put("18003", "小翠");
		return mapjavaEE;
		
	}

	//定义一个集合存储所有班级信息
	private static Map<String, Map<String, String>> Class() {
		// TODO Auto-generated method stub
		Map<String,Map<String,String>> mapclass = new LinkedHashMap<String,Map<String,String>>();
		mapclass.put("基础班", JavaSE());
		mapclass.put("就业班", JavaEE());
			return mapclass;
	}
}
