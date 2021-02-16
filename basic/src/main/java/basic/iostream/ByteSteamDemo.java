package basic.iostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * 
 * @author Lenovo
 *
 */

public class ByteSteamDemo {
public static void main(String[] args) throws IOException {
	File file = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\network Nodes.xlsx");
	File fileRead = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\streamCopy.dbc");
//	fileStreamWrite(file);
//	fileStreamRead(file);
	fileStreamReadbyte(file);
//	filecopy(fileRead);
}	
	
	private static void fileStreamWrite(File file) {
		//声明对象，提升ops的作用域
		OutputStream ops = null;
		try {
		//创建FileOutputStream对象
		ops= new FileOutputStream(file,true);
		//void write(byte[] b)将字节数组中的数据写入到文件中，\r\n用于换行
		byte[] data = "\r\nasdfghjkl;qwertyuio;1234567:速度和看梦还是滴哦冰雪公主".getBytes();
		ops.write(data);
		}catch(IOException ex){//捕获IO异常并将异常信息输出
			System.out.println(ex);
			//防止运行时因拔出U盘等其他原因而写入失败，需要抛出运行时异常
			throw new RuntimeException("写入失败，请重试");
			
		}finally {//无论是否有异常资源必须关闭
			try {
				//由于关闭资源时也有可能遇到异常，因此需要捕获异常
			ops.close();
			}catch(IOException e) {
				throw new RuntimeException("资源关闭失败，请重试");
			}
		}
	}

	private static void fileStreamRead(File file) {
		InputStream fis = null;
		try {
			fis=new FileInputStream(file);
			int cha=0;
			while((cha=fis.read())!=-1) {
				//abstract int read()从文件中读取下一个字节，返回的时字节编码，需要转换成字符
				//这种方法不能读取中文
				System.out.print((char)cha);
			}
		} catch (IOException ex) {
			throw new RuntimeException("文件读取失败,请重试");
		}finally {
			try {
				fis.close();
			} catch (Exception e) {
				throw new RuntimeException("资源关闭失败，请重试");
			}
		}
	}

	private static void fileStreamReadbyte(File file) {
		FileInputStream fis = null;
		try {
			fis=new FileInputStream(file);
			//abstract int read(byte[] b)从文件中读取一定量的字节并将其存储在缓冲数组b，返回的时字节编码，需要转换成字符
			int len=0;
			byte[] b = new byte[1024];
			while((len=fis.read(b))!=-1) {
				//调用String(byte[] bytes, int offset, int length)通过使用平台的默认字符集解码指定的 byte子数组
				System.out.print(len+" : "+new String(b, 0, len));
			}
		} catch (IOException ex) {
			throw new RuntimeException("文件读取失败,请重试");
		}finally {
			try {
				fis.close();
			} catch (Exception e) {
				throw new RuntimeException("资源关闭失败，请重试");
			}
		}
	}

	private static void filecopy(File file) {
		FileInputStream fis =null;
		FileOutputStream fos =null;
		try {
			fis=new FileInputStream(file);
			fos =new FileOutputStream("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\streamCopy.dbc");
			int len = 0;
			byte[] buf = new byte[1024];
			while((len=fis.read(buf))!=-1) {
			fos.write(buf,0,len);
			}
		} catch (Exception e) {
			throw new RuntimeException("文件读取失败,请重试");
		}finally {
			try {
				fis.close();
				fos.close();
			} catch (Exception e) {
				throw new RuntimeException("资源关闭失败，请重试");
			}
		}
	}
}
