package basic.grammer;
import java.util.Random;
import java.util.Scanner;

public class Day05_CallName {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("--------随机点名器--------");
		//定义一个数组（容器）存储学生的名字
		String[] Students=new String[6];
		addStudentsName(Students);
		viewStudentsName(Students);
		System.out.println("有请"+randomStudentsName(Students));
	}
	
	//定义添加全班学生名字的方法:addSdudentsName
		public static void addStudentsName(String[] Students) {	
			for(int i = 0;i < Students.length;i++) {
				System.out.println("请输入第"+i+"个学生的名字");
//				System.out.printInt（）
				Students[i]=new Scanner(System.in).next();
			}
		}
	
	//定义预览全班学生名字的方法：viewStudentsName
		public static void viewStudentsName(String[] Students) {
			for(int i = 0;i < Students.length;i++) {
				System.out.println("第"+i+"个学生的名字是："+ Students[i]);
			}
		}
			
	
	//随机读取一个学生的名字的方法：randomStudentsName
		public static String randomStudentsName(String[] Students) {
			int index =new Random().nextInt(Students.length);
		return Students[index];
		}
}
