package basic.grammer;


import java.util.ArrayList;
import java.util.Random;


public class callName {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<studentClass> array =new ArrayList<studentClass> ();
		initeNameList(array);
		printStudents(array);
		randomStudents(array);
	}
	
	
	//名单初始化：向集合中添加学生
	public static void initeNameList(ArrayList<studentClass> array) {
		studentClass s1=new studentClass();
			s1.setName("小一");
			s1.setSex("Boy");
			s1.setID(182351);
			
			studentClass s2=new studentClass();
			s2.setName("小二");
			s2.setSex("Boy");
			s2.setID(182352);
			
			studentClass s3=new studentClass();
			s3.setName("小三");
			s3.setSex("Girl");
			s3.setID(182353);
			
			studentClass s4=new studentClass();
			s4.setName("小一");
			s4.setSex("Boy");
			s4.setID(182354);
			
			studentClass s5=new studentClass();
			s5.setName("小一");
			s5.setSex("Boy");
			s5.setID(182355);
			
			studentClass s6=new studentClass();
			s6.setName("小一");
			s6.setSex("Girl");
			s6.setID(182356);
			
			array.add(s1);
			array.add(s2);
			array.add(s3);
			array.add(s4);
			array.add(s5);
			array.add(s6);			
	}
	
	//显示所有学生信息
	public static void printStudents(ArrayList<studentClass> array) {
		System.out.println("18届Java春季班学生名单");
		System.out.println("学号	姓名	性别");
		for(int i=0;i<array.size();i++) {
			studentClass s = array.get(i);
			System.out.println(s.getID()+"     "+s.getName()+"     "+s.getSex());
		}
	}
	
	//随机生成一个对象
	public static void randomStudents(ArrayList<studentClass> array) {
		Random r = new Random();
		int n =r.nextInt(array.size());
		studentClass s = array.get(n);
		System.out.println("被抽到的同学是：");		
		System.out.println(s.getID()+"     "+s.getName()+"     "+s.getSex());	
	}
	

	
}
