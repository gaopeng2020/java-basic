package patterns.structure.composite;

public class Client {

	public static void main(String[] args) {
		// 从大到小创建对象 学校
		Organization university = new University("清华大学", " 中国顶级大学 ");

		// 创建 学院
		Organization computerCollege = new College("计算机学院", " 计算机学院 ");
		Organization infoEngineercollege = new College("信息工程学院", " 信息工程学院 ");
		
		// 将学院加入到 学校
		university.add(computerCollege);
		university.add(infoEngineercollege);

		// 创建计算机学院的专业
		computerCollege.add(new Major("软件工程", " 软件工程不错 "));
		computerCollege.add(new Major("网络工程", " 网络工程不错 "));
		computerCollege.add(new Major("计算机科学与技术", " 计算机科学与技术是老牌的专业 "));

		//创建信息工程学院专业
		infoEngineercollege.add(new Major("通信工程", " 通信工程不好学 "));
		infoEngineercollege.add(new Major("信息工程", " 信息工程好学 "));
//		infoEngineercollege.print();

		 university.print();
	}

}
