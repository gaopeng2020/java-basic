package basic.iostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class propertiesDemo {
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\111.properties");
		propertiesStrore(file);
		propertiesload(file);
	}

	private static void propertiesStrore(File file) throws IOException {
		Properties prop = new Properties();
		prop.setProperty("name", "abc");
		prop.setProperty("age", "20");
		prop.setProperty("sex", "male");
		prop.setProperty("Addrese", "China");
		prop.setProperty("Phone", "13838384388");
		prop.setProperty("E-mail", "abc@abc.com");
		FileOutputStream fos = new FileOutputStream(file);
		prop.store(fos, null);
		fos.close();  
		
	}

	private static void propertiesload(File file) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(file);
		prop.load(fis);
		Set<String> keyset = prop.stringPropertyNames();
		fis.close();
		for(String key:keyset) {
			String value = prop.getProperty(key);
			System.out.println(key+" : "+value);
		}
		System.out.println(prop);
	}
}
