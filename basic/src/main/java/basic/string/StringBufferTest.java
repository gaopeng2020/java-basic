package basic.string;

public class StringBufferTest {
	public static void main(String[] args) {
		int[] arry= {65,66,67,68,69,70,71,72};
		String b =Buffer(arry);
		System.out.println(b);
	}
	
	
	public static String Buffer(int[] arry) {
		StringBuffer sb =new StringBuffer();
		sb.append("[");
		for(int i=0;i<arry.length;i++) {
			if(i==arry.length-1) {
				sb.append(arry[i]+"]");
			}
			else {
				sb.append(arry[i]+",");
			}
		}
		return sb.toString();
	}
}
