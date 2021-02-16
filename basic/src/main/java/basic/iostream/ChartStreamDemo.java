package basic.iostream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChartStreamDemo {

	public static void main(String[] args) {
		File file = new File("D:\\Users\\Lenovo\\Desktop\\test","log.log");
//		LogCategory.ADD;
		String strToPrint = "这是�?个测试日志文�?";
		LogGravity gravity = LogGravity.INFO;
		for (int i = 0; i <10000; i++) {
			PrintToLog(file, gravity, strToPrint, true);
		}
//		fileReader(file);
//		fileCopybychar(file);
	}

	/**
	 * @param file
	 * @param gravity
	 * @param path
	 * @param b
	 */
	private static void PrintToLog(File file, LogGravity gravity, String strToPrint, boolean b) {
		FileWriter fos = null;
		Date date = new Date();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss:SSS");
		String currentTime = dataFormat.format(date);
		try {
			fos = new FileWriter(file, true);
			String buffer = currentTime + " [" + gravity + "] " + strToPrint;
			fos.write(buffer);
			fos.write("\r\n");
			fos.flush();

			if (b) {
				fos.write("\r\n");
				fos.write("===================" + currentTime + "===================");
				fos.write("\r\n");
				fos.flush();
			}

		} catch (Exception e) {
			throw new RuntimeException("It seems that the log does not exist");
		} finally {
			try {
				fos.close();
			} catch (Exception e2) {
				throw new RuntimeException("Cannot close the streem");
			}
		}
	}
	
	  /**
	   * 日志文件中消息类型的枚举
	   *
	   * @return
	   */
	private static enum LogGravity {
		ADD, DELETE, MODIFY, INFO, ERRO
	}

	private static void fileReader(File file) {
		long start = System.currentTimeMillis();
		FileReader fis = null;
		try {
			fis = new FileReader(file);
			int ch = 0;
			char[] cbuf = new char[10240];
			while ((ch = fis.read(cbuf)) != -1) {
				System.out.print(new String(cbuf, 0, ch));
			}
		} catch (Exception e) {
			throw new RuntimeException("���ݶ�ȡʧ��");
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
				throw new RuntimeException("���ݶ�ȡʧ��");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("��ȡ�ļ���������:" + (end - start) + "ms");
	}

	private static void fileCopybychar(File file) {
		FileReader fis = null;
		FileWriter fos = null;
		try {
			fis = new FileReader(file);
			fos = new FileWriter("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\1234.txt");
			int ch = 0;
			char[] cbuf = new char[1024];
			while ((ch = fis.read(cbuf)) != -1) {
				fos.write(cbuf, 0, ch);
				fos.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException("����ʧ��");
		} finally {
			try {
				fos.close();
				fis.close();
			} catch (Exception e2) {
				throw new RuntimeException("����ʧ��");
			}
		}
	}

}
