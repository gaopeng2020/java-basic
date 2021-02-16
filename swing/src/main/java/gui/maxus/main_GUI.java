/**
 * 
 */
package gui.maxus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.WindowConstants;

/**
 * @author Administrator
 *
 */
public class main_GUI {

	/**
	 * Launch the application.
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		List<String> ecuInterfaces = new ArrayList<String>();
		ecuInterfaces.add("ECU1");
		ecuInterfaces.add("ECU2");
		ecuInterfaces.add("ECU3");
		ecuInterfaces.add("ECU4");
		ecuInterfaces.add("ECU5");

		Object[] ecuIfNames = new String[ecuInterfaces.size()];
		for (int i = 0; i < ecuInterfaces.size(); i++) {
			ecuIfNames[i] = ecuInterfaces.get(i);
		}

		// ʹ��ͬ��������ʹ��ǰ�߳�������ֱ�������⿪���յ�Notify��
//		diagCANMsgTest(ecuIfNames);
//		diagPushCANMsgTest(ecuIfNames);

		/*
		 * CountDownLatchʹ������ȴ��ӳ������н���
		 * CountDownLatch��һ��ͬ���������ߣ�����ʹһ�������̵߳ȴ�����������ֱ�������߳��е����������
		 * CountDownLatch�����ø�����count��һ��int���͵Ĵ��ڵ���0��ֵ�����г�ʼ����
		 * ����await������ʹ�߳�������ֱ����ǰ������countֵ�������㣻
		 * CountDownLatch�����Ϊֻ�ܴ���һ�Σ���Countֵ�����й������޷����ã�
		 * ����һ��CountDownLatch��ָ��count��ֵΪN����ô���CountDownLatch���������һ���̵߳ȴ�����N���߳̽�����
		 * ����countDown��������Ϊ����߳̽�������
		 */
//		jTablePanelTest();

		// DiagramSizeSetting
//		diagramSizeTest();

		// ʹ�ÿ�Bug��ʽ��ʹ�����������ѭ��ֱ����������
		nmCanMsgTest();

		System.out.println("===================main======================");
		
	}

	private static void diagramSizeTest() {
		Thread currentThread = Thread.currentThread();
		DiagramSizeSettingDialog dialog = null;
		try {
			dialog = new DiagramSizeSettingDialog(currentThread);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (currentThread) {
			try {
				currentThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void jTablePanelTest() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		JTablePanelDemo dialog = new JTablePanelDemo(countDownLatch);
		try {
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		countDownLatch.await();

		System.out.println("========jTablePanelTest=======");
		System.out.println(dialog.pageIndex);
	}

	/**
	 * 
	 */
	private static void nmCanMsgTest() {
		Thread currentThread = Thread.currentThread();
		NMCanMsgInutDialog dialog = new NMCanMsgInutDialog(currentThread);
		try {
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		synchronized (currentThread) {
			try {
				currentThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		long startTime = System.currentTimeMillis();
//		while (true) {
//			System.out.println("");
//			if (dialog.messageID != -1) {
//				break;
//			}
//			if (dialog.isCancel) {
//				return;
//			}
//			// The Loop will exit after 60s
//			long endTime = System.currentTimeMillis();
//			if ((endTime - startTime) / 1000 > 60) {
//				return;
//			}
//		}
		
		System.out.println("nmAsrNodeIdentifier = "+dialog.nmNodeAddress);
		System.out.println("nmAsrwaitBusSleepTime = "+dialog.nmAsrWaitBusSleepTime);
		System.out.println("nmAsrBaseAddress = "+dialog.nmBaseAddress);

	}

	/**
	 * @param ecuIfNames
	 * @throws InterruptedException
	 */
	private static DiagPushCanMsgInputDialog diagPushCANMsgTest(Object[] ecuIfNames) {
		Thread currentThread = Thread.currentThread();
		DiagPushCanMsgInputDialog dialog = new DiagPushCanMsgInputDialog(ecuIfNames, currentThread);
		try {
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (currentThread) {
			try {
				currentThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(dialog.byteOder);
		return dialog;
	}

	/**
	 * @param ecuIfNames
	 * @throws InterruptedException
	 * 
	 */
	private static void diagCANMsgTest(Object[] ecuIfNames) {
		Thread currentThread = Thread.currentThread();
		DiagCanMsgInputDialog dialog = new DiagCanMsgInputDialog(ecuIfNames, currentThread);
		try {
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (currentThread) {
			try {
				currentThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("comType = " + dialog.comType);
		System.out.println("comType = " + dialog.byteOrder);
		if (dialog.receicedECUs == null) {
			return;
		}
		List<Object> receicedECUs = dialog.receicedECUs;
		for (Object object : receicedECUs) {
			System.out.println(object.toString());
		}

	}

}