package InvokeCmd;

public class Test {
    public static void main(String[] args) {
        String path = "D:\\ProgramFiles\\DeveloperKits\\cmake\\cmake-3.25.1\\bin";
        Thread currentThread = Thread.currentThread();

        JavaInvokeCmd swrs = new JavaInvokeCmd(currentThread);
        swrs.inputCommand("d:");
        swrs.inputCommand("cd " + path);
        swrs.inputCommand("ipconfig");
        swrs.inputCommand("cmake -version");
        swrs.inputCommand("chdir");
        Process process = swrs.getProcess();
        synchronized (currentThread) {
            try {
                currentThread.wait();
                System.out.println("----------------------------------------------------------------------");
                process.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
