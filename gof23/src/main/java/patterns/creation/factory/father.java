package patterns.creation.factory;

public class father {
    public static void main(String[] args) {
        new father();
    }

    public father() {
        System.out.println("father");
        new son();
    }
}
