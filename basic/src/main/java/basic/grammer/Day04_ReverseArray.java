package basic.grammer;

public class Day04_ReverseArray {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double [] array = {1.2,2.5,3.8,4.6,5.99,6.6,7.3};
		reverseArray(array);
		printArray(array);
	}

	
	
	//定义一个arrayReverse方法实现数组逆序的功能：与最远端互换
	public static void reverseArray(double[] array) {
		//用指针的思想+临时变量互换数据
		for(int min =0, max=array.length-1; min<max ;min++,max--) {
		double temp = array[min];
		array[min]=array[max];
		array[max]=temp;
		}		
	}
	
	
	
	//用遍历的方法打印数组输出的功能，供main方法调用，无返回值
	public static void printArray(double[] array) {
		System.out.print("[");
		for(int i=0; i<array.length;i++) {
			if (i<array.length-1) {
			System.out.print(array[i]+",");	
			}
			else {
				System.out.print(array[i]+"]");
				}
			}
	}
	
	
				
	} //class类
