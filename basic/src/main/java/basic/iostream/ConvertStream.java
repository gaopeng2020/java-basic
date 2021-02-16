package basic.iostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConvertStream {

	public static void main(String[] args) throws IOException {
		File file = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\FileInputStream.txt");
//		outputstreamwriter(file);
		inputstreamreader(file);
	}

	private static void outputstreamwriter(File file) throws IOException {
		//���ַ�����ָ���ı����ת���ֽڣ���ʹ���ֽ�������Щ�ֽ�д��ȥ
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		osw.write("���java\r\n");
		osw.write(100);
		osw.write("\r\n");
		char[] cbuf = {'c','d','e','f'};
		osw.write(cbuf);
		osw.write("\r\nһ��Сʱ�����������", 0, 13);
		osw.close();
	}

	private static void inputstreamreader(File file) throws IOException {
		InputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"GBK");
		int len = 0;
		char [] cbuf = new char[10240];
		while((len=isr.read(cbuf))!=-1) {
			System.out.print(new String(cbuf, 0, len));
		}
		isr.close();
	}

}
