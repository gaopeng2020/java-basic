package basic.grammer;

public class Day04_bubblingSort {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	double[] array= {1.2,4.5,2,3,6.6,100,8.8,55,10,78,99,108,16,20,201,129,169,198};
	bubblingSort(array);
	printArray(array);
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
	
	
}
