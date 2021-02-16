package basic.grammer;
import java.util.ArrayList;
import java.util.Scanner;

public class Day08_SupermarketGoodsMgmAPP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Day08_SupermarketAPPClass> array = new ArrayList<Day08_SupermarketAPPClass>();
		
		initGoodsInfo(array);
		
		while(true) {
			int customerChoice = selectiveMenu();
			switch(customerChoice) {
			case 1:
				viewGoods(array);
				break;
			case 2:
				addGoods(array);
				break;
			case 3:
				removeGoods(array);
				break;	
			case 4:
				modifyGoods(array);
				break;				
			case 5:
				return;
			default:
				System.out.println("Please input the options of 1,2,3");	
				break;
			}
		}

	}
	
	//initGoodsInfo方法
	public static void initGoodsInfo(ArrayList<Day08_SupermarketAPPClass> array) {
		Day08_SupermarketAPPClass g1=new Day08_SupermarketAPPClass();
		Day08_SupermarketAPPClass g2=new Day08_SupermarketAPPClass();
		Day08_SupermarketAPPClass g3=new Day08_SupermarketAPPClass();
		g1.ID=1001;
		g1.name="TestGoods1";
		g1.price=10.01;
		g1.quantity=20;
		
		g2.ID=1002;
		g2.name="TestGoods2";
		g2.price=12.99;
		g1.quantity=20;
		
		g3.ID=1003;
		g3.name="TestGoods3";
		g3.price=19.99;
		g3.quantity=20;
		array.add(g1);
		array.add(g2);
		array.add(g3);
		
	}
	
	
	//selectiveMenu 方法
	public static int selectiveMenu() {
		System.out.println("==================欢迎光临乐天超市===================");
		System.out.println("1:查看货物清单");
		System.out.println("2:添加新货物");
		System.out.println("3:删除货物");
		System.out.println("4:修改货物");
		System.out.println("5:退出系统");
		System.out.println("请输入功能序号");
		Scanner sc = new Scanner(System.in);
		return sc.nextInt();
		
	}
	
	//viewGoods 方法：制作表头并打印商品信息
	public static void viewGoods(ArrayList<Day08_SupermarketAPPClass> array) {
		System.out.println("==================乐天超市商品清单===================");
		System.out.println("商品ID"+"	"+"商品名字"+"	"+"商品价格"+"	"+"库存数量");
		for(int i=0;i<array.size();i++) {
			Day08_SupermarketAPPClass printGoods = array.get(i);
			System.out.println(printGoods.ID+"	"+printGoods.name+"	"+printGoods.price+"	"+printGoods.quantity);
		}
	}
	
	//addGoods 方法
	@SuppressWarnings("resource")
	public static void addGoods(ArrayList<Day08_SupermarketAPPClass> array) {
		System.out.println("请输入要添加的产品商品信息");
		//定义一个addgoods的Day08_SupermarketAPPClass类
		Day08_SupermarketAPPClass addgoods =new Day08_SupermarketAPPClass();
		//通过用户输入向addgoods类添加
		System.out.println("请输入要添加的商品ID");
		addgoods.ID=new Scanner(System.in).nextInt();
		System.out.println("请输入要添加的商品名字");
		addgoods.name=new Scanner(System.in).nextLine();
		System.out.println("请输入要添加的价格");
		addgoods.price=new Scanner(System.in).nextDouble();
		System.out.println("请输入要添加的库存数量");
		addgoods.quantity=new Scanner(System.in).nextInt();	
		//将addgoods添加到集合
		array.add(addgoods);
	}
	
	//removeGoods 方法
	public static void removeGoods(ArrayList<Day08_SupermarketAPPClass> array) {
		System.out.println("请输入要删除商品的编号");
		int ID = new Scanner(System.in).nextInt();
		for(int i = 0; i<array.size(); i++) {
			Day08_SupermarketAPPClass removegoods = array.get(i);
			if(removegoods.ID==ID) {
				array.remove(removegoods);
			}
		}
	}
	
	//modifyGoods方法
	public static void modifyGoods(ArrayList<Day08_SupermarketAPPClass> array) {
		System.out.println("请输入要修改商品的编号");
		int ID = new Scanner(System.in).nextInt();
		for(int i = 0; i<array.size(); i++) {
			Day08_SupermarketAPPClass modifygoods = array.get(i);
			if(modifygoods.ID==ID) {
				modifygoods.name=new Scanner(System.in).nextLine();
				modifygoods.price=new Scanner(System.in).nextDouble();
				modifygoods.quantity=new Scanner(System.in).nextInt();	

			}
		}		
	}
	
}



