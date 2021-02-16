package basic.string;

public class stringend {
public static void main(String[] args) {
	String str = "Front Sensor Power";
	// boolean endsWith(String suffix) 测试此字符串是否以指定的后缀结束。 
	boolean b = str.endsWith("Power");
	System.out.println(b);
}
}
