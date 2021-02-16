package basic.grammer;
import java.util.Scanner;

public class Day05_ProductInventoryMgn {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//定义变量 brand,size,price,inventerty
		String [] brand = {"MacBookAir", "ThinkpadT450","Dell7860"};
		double [] size = {13.3,14,15.6};
		double [] price = {8888.88,6666.99,7999.99};
		int [] inventory = {0,0,0};
		
		while(true) {
		int choice = chosefuction();
		switch(choice) {
		case 1 :
			printProductInfo (brand,size,price,inventory);
		break;
		
		case 2:
			updateIventory(brand,inventory);
		break;
		
		case 3:
			return;
		
		default :
			System.out.println("Please input the options of 1,2,3");	
		break;
		
		}
		
		}
	}

	//实现选择功能的方法：接受键盘输入的数字而返回给方法
	public static int chosefuction() {
		System.out.println("-------------库存管理------------");
		System.out.println("1.查看库存清单");
		System.out.println("2.修改商品库存数量");
		System.out.println("3.退出");
		System.out.println("请输入要执行的操作序号：");
		@SuppressWarnings("resource")
		int choice= new Scanner(System.in).nextInt();
		return choice;
	}
	
	//实现查看产品库存清单功能的方法：遍历数组打印商品信息  case1
	public static void printProductInfo(String[] brand, double size[], double price[], int inventory[]) {
		int totalCount = 0;
		double totalmoney = 0.0;
		System.out.println("------Product Inventoty information------");
		System.out.println("brand		size	price	inventory");
		for(int i=0;i<brand.length;i++) {
			System.out.println(brand[i]+"	"+size[i]+"  "+price[i]+"    "+inventory[i]);
			totalCount += inventory[i]; //计算每个品牌产品的库存
			totalmoney += inventory[i]*price[i];  //计算每个品牌库存的总价格			
		}
		System.out.println("Total Inventory:"+totalCount);
		System.out.println("Total Inventory Price:"+totalmoney);
	}
	
	//实现在线动态修改商品的库存数的功能方法 case2
	public static void updateIventory(String[] brand,int inventory[]) {
		for (int j= 0; j< brand.length; j++) {
			System.out.println("Please input the latest inventory of "+brand[j]);
			inventory[j]=new Scanner(System.in).nextInt();
		}	
	}
	
	
}
