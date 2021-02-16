package basic.thread.waitnotify;

public class Resource {
private String name;
private String sex;
boolean flag = false;

public synchronized void setContent(String name,String sex) {
	if(flag) { 
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();}
		}else {
		this.name = name;
		this.sex = sex;
		flag=true;
		this.notify();
		}
}


public synchronized void getContent() {
	if(!flag) {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}else {
		System.out.println(this.name+"   "+this.sex);
		flag = false;
		this.notify();
		}
}

}
