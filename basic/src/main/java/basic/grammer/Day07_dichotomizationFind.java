package basic.grammer;
import java.util.Scanner;

public class Day07_dichotomizationFind {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[] array= {1.2,4.5,2,3,6.6,100,8.8,55,10,78,99,108,16,20,201,129,169,198};
		//用户输入要查找的值
		System.out.println("请输入要查找的值");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		double key = sc.nextDouble();
		
		//先对数组升序才能用二分法
		bubblingSort(array); 
		//将数组array以及用户输入值"key"赋值给dichotomizationFind方法
		dichotomizationFind(array,key);
		
		double k = dichotomizationFind(array, key);
		if(k == -1) {
			System.out.println("找不到匹配值");
			}else {
				System.out.println("要查找的值索引为："+k);
		}
		
		
	}
	
	
	
	//冒泡排序法（升序）：每相邻的两个元素对比，将较大的放后面，小的放前面
	public static void bubblingSort(double[] array) {
		for(int i =1;i<array.length-1;i++) {//
			for(int j=0;j<array.length-i;j++) {//相邻的两个元素互换后内层循环实现
				if(array[j]> array[j+1]) {
					double temp = array[j];
					array [j] = array [j+1];
					array [j+1] = temp;
				}
			}
		}
	}
	
	
	

	/* 二分法查找反方法(数组必须升序排列)：返回值：要查找元素的索引值，整数变量；
	         二分法实现原理：首先定位到 数组数组的二分位即 array[mid]=array[（min+max)/2]，
	         	     然后对比要查找值与二分位数组值：查找值大于二分位值，min=mid+1, 此时二分位索引为：mid=（min(new)+max)/2
	         	     				         查找值小于二分位值，max=mid-1， 此时二分位索引为：mid=（min+max(new))/2
	         	  					         查找值=二分位值，返回二分位索引。
	         	  					         当min=max也找不到要查找的值，循环结束，返回找不到信息。
	 */
	public static double dichotomizationFind(double[] array, double key) {
		int min = 0,max=array.length,mid=(min+max)/2;
		while(min<=max) {
			if(key>array[mid]) {
			min=mid+1;	
			}else if(key<array[mid]) {
				max=mid-1;
			}else {
				return mid;
			 }
			mid=(min+max)/2;	
			}
		return -1;

		}

		
}
