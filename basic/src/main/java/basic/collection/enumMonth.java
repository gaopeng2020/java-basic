package basic.collection;

public enum enumMonth{
	JAN("一月",1),FEB("二月",2),MAR("三月",3),APR("四月",4),MAY("五月",5),JUN("六月",6),
	JUL("七月",7),AUG("八月",8),SEP("九月",9),OCT("十月",10),NOV("十一月",11),DEC("十二月",12);
	
	private String month;
	private int value;
	
	private enumMonth(String month, int value) {
		this.month = month;
		this.value = value;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.month;
	}
	
	
}

