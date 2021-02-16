package proxySkeleton.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PersonSkeleton extends Thread {
	private PersonServer myServer;

	public PersonSkeleton(PersonServer server) {
		// get reference of object server
		this.myServer = server;
	}

	public void run() {
		try {
			// new socket at port 9000
			ServerSocket serverSocket = new ServerSocket(9000);

			// accept Client's request
			Socket socket = serverSocket.accept();
			while (socket != null) {
				// get proxy's request
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				String method = (String) inStream.readObject();

				// check method name
				if (method.equals("age")) {
					// execute object server's business method
					int age = myServer.getAge();
					ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
					// return result to proxy
					outStream.writeInt(age);
					outStream.flush();
				}

				if (method.equals("name")) {
					// execute object server's business method
					String name = myServer.getName();
					ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
					// return result to stub
					outStream.writeObject(name);
					outStream.flush();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(0);
		}
	}
}
