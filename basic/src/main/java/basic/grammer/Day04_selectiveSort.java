package basic.grammer;

public class Day04_selectiveSort {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double [] array = {1.2,6,8,4.6,5.99,9,7.3};
		selectiveSort(array);
		printArray(array);
		
	}

	
	// 方法：选择排序(升序），根据每一个数组元素与其后面所有元素的对比将较大的放后面，小的放前面。
	public static void selectiveSort(double[] array) {
		
	for(int i =0;i<array.length-1;i++) {
		for(int j=i+1;j<array.length;j++) {
			if(array[i] > array[j]) {
				double temp = array[i];
				array[i]=array[j];
				array[j]= temp;
				
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
