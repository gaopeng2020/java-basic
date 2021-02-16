package proxySkeleton.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import proxySkeleton.service.Person;

//PersonProxy和PersonServer一样，都implements Person。它们都实现了getAge()和getName()两个business method，
//不同的是PersonServer是真的实现，PersonProxy是需要建立socket连接，并向Skeleton发请求，
//然后通过Skeleton调用PersonServer的方法，最后接收返回的结果。
public class PersonProxy implements Person {

	private Socket socket;

	public PersonProxy() throws Throwable {
		// connect to skeleton
		socket = new Socket("computer_name", 9000);
	}

	@Override
	public int getAge() throws Throwable {
		//Serialization and send method name to skeleton
		ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
		outStream.writeObject("age");
		outStream.flush();
		
		//deserialization and received data from skeleton
		ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
		return inStream.readInt();
	}

	@Override
	public String getName() throws Throwable {
		// pass method name to skeleton
		ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
		outStream.writeObject("name");
		outStream.flush();
		ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
		return (String) inStream.readObject();
	}

}
