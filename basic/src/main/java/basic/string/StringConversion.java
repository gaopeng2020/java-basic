package basic.string;

import java.util.regex.Pattern;

public class StringConversion {
	public static void main(String[] args) {
		// 正则表达式
//		String num = "2000";
//		String str = "1: abcd";
//		String regex1 = "\\d{1,}";
//		String regex2 = "[0-9]: ";
//		boolean b1 = num.matches(regex1);
//		boolean b2 = str.matches(regex2);
//		String string = str.replaceAll(regex2, "");
//		System.out.println(string);
//
//		String qq = "604154942";
//		String regex = "[1-9][0-9]{4,14}";
//		boolean flag2 = qq.matches(regex);

		// 将十六进制转换为10进制
		String string16 = "0x8001";
		int dec = hextoDec(string16)-32768;
		System.out.println(dec);

		// 去除字符串中的前导与尾部空格、制表符、换行于回车
		String nrst = " T es t" + "1	2	3";
		String replaceAll = FormatString(nrst);

//		// intel到Motorola格式转换
//		int intel = 15;
//		int toMotorola = interlToMotorola(intel);
//
//		// startWith与endwith测试
//		String oldSignalName = "ABAActv_BCU_07_ABAActv_BCU_07";
//		String startWith = "ACM_01_CRC";
//		String interfaceName = "BCU_07\\w{0,}";
//		boolean start = oldSignalName.startsWith(startWith);
//		String signal = oldSignalName.replaceAll("_"+interfaceName, "");
//		System.out.println(start);
//		System.out.println("==========="+signal);
	}

	/*
	 * intel到Motorola格式转换
	 */
	public static int InterlToMotorola(int intel) {
		int mod = intel % 8;
		int motorola;
		switch (mod) {
		case 0:
			motorola = intel + 7;
			return motorola;
		case 1:
			motorola = intel + 5;
			return motorola;
		case 2:
			motorola = intel + 3;
			return motorola;
		case 3:
			motorola = intel + 1;
			return motorola;
		case 4:
			motorola = intel - 1;
			return motorola;
		case 5:
			motorola = intel - 3;
			return motorola;
		case 6:
			motorola = intel - 5;
			return motorola;
		case 7:
			motorola = intel - 7;
			return motorola;
		}
		return -1;
	}

	/**
	 * 去除字符串中的前导与尾部空格、制表符、换行于回车 \n 回车 \t 水平制表符\s 空格 \r 换行
	 * 
	 * @param signalName
	 * @return
	 */
	public static String FormatString(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s|\t|\r|\n");
			java.util.regex.Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest.trim();
	}
	
	/*
	 * @Description:Transfer hexadecimal to decimal
	 * @param str String
	 * @return decimal int
	 */
	public static int hextoDec(String str) {
		if (str == null || str.equals("")) { //$NON-NLS-1$
			return -1;
		}
		String hexadecimal = str.replaceAll("0x", ""); //$NON-NLS-1$ //$NON-NLS-2$
		Integer integer = Integer.valueOf(hexadecimal, 16);
		int decimal = integer.intValue();
		return decimal;
	}
}
