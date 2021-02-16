package jdbc;
public class ReceiveDataFromMySQL {
	private int sid;
	private String sname;
	private double sprice;
	
	public ReceiveDataFromMySQL() {
		super();
	}

	public ReceiveDataFromMySQL(int sid, String sname, double sprice) {
		super();
		this.sid = sid;
		this.sname = sname;
		this.sprice = sprice;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public double getSprice() {
		return sprice;
	}

	public void setSprice(double sprice) {
		this.sprice = sprice;
	}

	@Override
	public String toString() {
		return "[sid=" + sid + ", sname=" + sname + ", sprice=" + sprice + "]";
	}
	
	
}
