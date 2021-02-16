package basic.collection;

import basic.collection.enumerate.month;

public class enumtest {

	private static enumMonth dec;

	public static void main(String[] args) {
		month month = enumerate.month.APR;
		int value = 0;
		switch (month) {
		case JAN:
			value=1;
			break;
		case FEB:
			value=2;
			break;
		case MAR:
			value=3;
			break;
		case APR:
			value=4;
			break;
		case MAY:
			value=5;
			break;
		case JUN:
			value=6;
			break;
		case JUL:
			value=7;
			break;
		case AUG:
			value=8;
			break;
		case SEP:
			value=9;
			break;
		case OCT:
			value=10;
			break;
		case NOV:
			value=11;
			break;
		case DEC:
			value=12;
			break;
		default:
			break;
		}
		System.out.println(value);
		dec = enumMonth.DEC;
		System.out.println(dec);
	}

}
