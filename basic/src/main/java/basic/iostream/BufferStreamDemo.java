package basic.iostream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BufferStreamDemo {

	public static void main(String[] args) throws IOException {
		File sourcceFile = new File("D:\\Users\\Lenovo\\"
				+ "eclipse_workspace\\iotest\\NewFolerCreatedByJava\\abc\\test.txt");
		File destnationFile = new File("D:\\Users\\Lenovo\\eclipse_"
				+ "workspace\\iotest\\NewFolerCreatedByJava\\abc\\copy_test.txt");
		bufferedoutputstream(sourcceFile);
		bufferedinputstream(sourcceFile);
		bufferedinputoutputstreamcopy(sourcceFile,destnationFile);
		
		bufferedwriter(sourcceFile);
		bufferedreader(sourcceFile);
		bufferedreaderwritercopy(sourcceFile,destnationFile);
	}

	private static void bufferedoutputstream(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos =new BufferedOutputStream(fos);
		byte[] arg0 = "HelloJava:Helloworld:探索软件世界的大门".getBytes();
		for(int i = 0; i<arg0.length;i++) {
		System.out.println("======="+arg0[i]);
		}
		bos.write(arg0);
		bos.write("\r\nJava程序设计很复杂，有点操蛋\r\n".getBytes());
		bos.write(65);
		bos.close();
	}

	private static void bufferedinputstream(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis =new BufferedInputStream(fis);
		int len =0;
		byte[] bbuf =new byte[1024];
		while((len=bis.read(bbuf))!=-1) {
			System.out.println(new String(bbuf,0,len));
		}
		bis.close();
	}


	private static void bufferedinputoutputstreamcopy(File file, File des) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des)) ;
		int len = 0;
		byte[] buf = new byte[1024];
		while((len=bis.read(buf))!=-1) {
			bos.write(buf);
		}
		bis.close();
		bos.close();
	}
	
	private static void bufferedwriter(File file) throws IOException {
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw =new BufferedWriter(fw);
		bw.write("void write(String s)直接写入字符串：字符缓冲输出流 BufferedWriter");
		bw.newLine();
		char[] cbuf ="void write(char[] cbuf)写入字符数组 ".toCharArray(); 
		bw.write(cbuf);
		bw.close();
	}

	private static void bufferedreader(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		//方法一：调用readLine判断文本最后一行，返回null
/*		String line = null;
		while((line=br.readLine())!=null) {
			System.out.println(line);
		}*/
		//方法二：用read方法读取文本最后一个字符返回-1
		int len = 0;
		char[] cbuf = new char[1024];
		while((len=br.read(cbuf))!=-1) {
			System.out.println(new String(cbuf));
		}
		br.close();
		
	}

	private static void bufferedreaderwritercopy(File file, File des) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		BufferedWriter bw =new BufferedWriter(new FileWriter(des));
		int len =0;
		char[] cbuf = new char[1024];
		while((len=br.read(cbuf))!=-1) {
			bw.write(cbuf);
		}
		
		br.close();
		bw.close();
	}


}
