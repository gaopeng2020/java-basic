package basic.iostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableDemo {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		File file = new File("D:\\Backup\\eclipse_workspace\\iotest\\111.txt");
		objectoutputstream(file);
		objectinputstream(file);
		
	}

	private static void objectoutputstream(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		SerializablePerson p1 = new SerializablePerson("abc", 20, "male");
		SerializablePerson p2 = new SerializablePerson("abd", 21, "female");
		SerializablePerson p3 = new SerializablePerson("abe", 22, "female");
		oos.writeObject(p2);
		oos.writeObject(p1);
		oos.writeObject(p3);
		oos.writeObject(new SerializablePerson("abf", 25, "male"));
		oos.writeObject(new SerializablePerson("abg", 24, "male"));
		oos.close();
	}

	private static void objectinputstream(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		SerializablePerson p1 =(SerializablePerson) ois.readObject();
		SerializablePerson p2 =(SerializablePerson) ois.readObject();
		SerializablePerson p3 =(SerializablePerson) ois.readObject();
		System.out.println(p1.toString()+p2.toString()+p3.toString());
	}

}
