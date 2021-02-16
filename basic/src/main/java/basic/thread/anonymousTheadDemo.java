package basic.thread;

/*
 *  ʹ�������ڲ���,ʵ�ֶ��̳߳���
 *  ǰ��: �̳л��߽ӿ�ʵ��
 *  new ������߽ӿ�(){
 *     ��д���󷽷�
 *  }
 */
public class anonymousTheadDemo {
	public static void main(String[] args) {
		//�̳з�ʽ  XXX extends Thread{ public void run(){}}
		new Thread(){
			@Override
			public void run(){
				System.out.println("!!!");
			}
		}.start();
		
		//ʵ�ֽӿڷ�ʽ  XXX implements Runnable{ public void run(){}}
		
		Runnable r = new Runnable(){
			@Override
			public void run(){
				System.out.println("###");
			}
		};
		new Thread(r).start();
		
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				System.out.println("@@@");
			}
		}).start();
		
		
		//Lambda Expression
		new Thread(()-> {System.out.println("###");}).start();;
	}
}
