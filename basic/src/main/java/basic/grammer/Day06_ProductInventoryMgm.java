package basic.grammer;
import java.util.ArrayList;
import java.util.Scanner;

public class Day06_ProductInventoryMgm {

	public static void main(String[] args) {
		// 创建array集合存储Day06_ProductPropertyClass类型的数据
		ArrayList<Day06_ProductPropertyClass> array =new ArrayList<Day06_ProductPropertyClass>();	
		inputProductInfo(array);
		System.out.println(array.size());
	while(true) {
		int customerChoice = customerChoiceFunction();
		switch(customerChoice) {
		case 1:
			printProductInfo(array);
			break;
		case 2:
			modifyProductInfo(array);
			break;
		case 3:
			return;
		default:
			System.out.println("Please input the options of 1,2,3");	
			break;
		}
	}
	}
	
	
	//inputProductInfo方法：直接在集合array中添加产品信息无需返回给main方法，
	//并将定义的Day06_ProductPropertyClass类型的集合array传递给方法
	public static void inputProductInfo (ArrayList<Day06_ProductPropertyClass> array) {
	//创建Day06_ProductPropertyClass自定义类型的变量p1,p2,p3,p4
		Day06_ProductPropertyClass p1=new Day06_ProductPropertyClass();
		Day06_ProductPropertyClass p2=new Day06_ProductPropertyClass();
		Day06_ProductPropertyClass p3=new Day06_ProductPropertyClass();
		Day06_ProductPropertyClass p4=new Day06_ProductPropertyClass();
	//为三个变量赋值	
		p1.brand="MacBookAir";
		p1.CPU="Intel i7U";
		p1.ROM= "8G DDR3 1600";
		p1.size = 13.3;
		p1.color="White";
		p1.weigth="1.5KG";
		p1.price = 8888.88;
		p1.inventory = 100;
		
		p2.brand="ThinkpadT450";
		p2.CPU="Intel i5HD";
		p2.ROM= "8G DDR3 1600";
		p2.color="Black";
		p2.weigth="1.5KG";
		p2.size = 14;
		p2.price = 6666.99;
		p2.inventory = 100;
		
		p3.brand="Dell7860";
		p3.CPU="Intel i7HD";
		p3.ROM= "16G DDR3 1600";
		p3.size = 15.6;
		p3.color="Silver";
		p3.weigth="2.5KG";
		p3.price = 7999.99;
		p3.inventory = 100;
		
		p4.brand="HP Z860";
		p4.CPU="Intel i5HD";
		p4.ROM= "16G DDR3 1600";
		p4.size = 15.6;
		p4.color="Silver";
		p4.weigth="2.5KG";
		p4.price = 6999.99;
		p4.inventory = 100;
		//用array.get(变量）的方法为集合array添加数据
		array.add(p1);
		array.add(p2);
		array.add(p3);
		array.add(p4);
	}	
	
	
	//制作选择菜单的方法，需要将用户选择之返回到main中
	public static int customerChoiceFunction() {
		System.out.println("-------------库存管理------------");
		System.out.println("1.查看库存清单");
		System.out.println("2.修改商品库存数量");
		System.out.println("3.退出");
		System.out.println("请输入要执行的操作序号：");
		@SuppressWarnings("resource")
		int customerChoice= new Scanner(System.in).nextInt();
		return customerChoice;
	}
	
	
	// printProdctInfo方法：直接打印输出无需返回给main方法，将定义的Day06_ProductPropertyClass类的集合array传递给方法
	public static void printProductInfo (ArrayList<Day06_ProductPropertyClass> array) {
		@SuppressWarnings("unused")
		int totalCount = 0;
		@SuppressWarnings("unused")
		double totalMoney = 0.0;
		System.out.println("----------------Desktop Computer Inventoty information-----------------------");
		System.out.println("brand		CPU		  ROM		size	Color	Weigth	 price	inventory");
		//用For循环遍历集合array中的每个元素，用array.get得到集合array的 size
		for(int i = 0;i<array.size();i++) {
		//定义一个Day06_ProductPropertyClass类型的变量用于存储array（i）的数据
			Day06_ProductPropertyClass n = array.get(i);
			System.out.println(n.brand+"	"+n.CPU+"	"+n.ROM+"	"+n.size+"    "+n.color+"	"+n.weigth+"	"+n.price+"    "+n.inventory);
			totalCount += n.inventory; //计算每种品牌产品的库存
			totalMoney += n.inventory*n.price;  //计算每中品牌库存的总价格			
		}
			
		}
	
	
		//modifyProductInfo方法， 直接在集合array中修改无需返回给main方法，将定义的Day06_ProductPropertyClass类型的集合array传递给此方法
		public static void modifyProductInfo (ArrayList<Day06_ProductPropertyClass> array) {
			//用遍历循环将键盘输入值传递给array(i)
			for(int i = 0;i<array.size();i++) {
				//定义一个Day06_ProductPropertyClass类型的变量用于存储array（i）的数据
					Day06_ProductPropertyClass modify = array.get(i);
					System.out.println("Please input lastet inventory data of "+modify.brand);
					@SuppressWarnings("resource")
					Scanner sc = new Scanner(System.in);
					modify.inventory = sc.nextInt();
			}
		}

}
