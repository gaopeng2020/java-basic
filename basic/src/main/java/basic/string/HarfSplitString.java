package basic.string;

public class HarfSplitString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str1 = "Abc_FL_01_Abc_FL_01";
		String str2 = "Abc_Abc";
		harfSplitString(str2);

	}

	private static void harfSplitString(String str) {
		// TODO Auto-generated method stub
		int n = str.length();
		String substring = str.substring(0, n / 2);
		System.out.println(substring);
	}

}
