package basic.grammer;
import java.util.Scanner;

public class Day03_Print9x9 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("请输入要打印乘法表的阶数");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int n=sc.nextInt();
		print9x9(n);
	}
	
	
	public static void print9x9(int n) {
		for(int i =1; i<=n; i++) {
			for(int j=1; j<=i; j++) {
				System.out.print(j+"*"+i+"="+i*j+"\t");
			}
			System.out.println();
		}
	}

}
