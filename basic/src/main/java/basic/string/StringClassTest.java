package basic.string;

public class StringClassTest {



	public static void main(String[] args) {
		methord_1();
		int i = FindString("hellojava,nihaojava,javazhenbang,java.com,javaAPI","java");
		System.out.println(i);
	}



	private static void methord_1() {
		String str = "abcde";
		char[] chs = str.toCharArray();
		byte[] bytes = str.getBytes();
		System.out.println(chs);
		System.out.println(bytes);
		
	}
	
	
	private static int FindString(String big,String small) {
		int index;
		int count = 0;
		while((index=big.indexOf(small))!=-1) {
			count++;
			big=big.substring(index+small.length());
		}
		return count;
	}

}
