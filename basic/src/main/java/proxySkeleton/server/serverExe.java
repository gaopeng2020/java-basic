package proxySkeleton.server;

public class serverExe {

	public static void main(String[] args) {
		// new object server
		PersonServer person = new PersonServer(34, "Richard");
		PersonSkeleton skeleton = new PersonSkeleton(person);
		skeleton.run();
	}

}
