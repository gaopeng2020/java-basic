package projects.exportSwDocs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JavaInvokeCmd {
    private final InputStream inputStream;
    private final InputStream errorStream;
    private final OutputStream outputStream;
     private final Thread callerThread;
     @Getter
     private Process process;

    public JavaInvokeCmd(Thread callerThread) {
        this.callerThread = callerThread;
        try {
            process = Runtime.getRuntime().exec("cmd");
            inputStream = process.getInputStream();
            outputStream = process.getOutputStream();
            errorStream = process.getErrorStream();

            new Thread(this::readCmdOut).start();
            new Thread(this::readCmdError).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 运行命令
    public void inputCommand(String cmd) {
        try {
            // 若执行某些命令出现异常，可以尝试更换 \n 为 \r 或 \n\r
            outputStream.write((cmd + "\n").getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("命令执行错误：" + cmd, e);
        }
    }

    private void readCmdOut() {
        try {
            print(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("获取输出流异常", e);
        }
        System.out.println("正常输出流结束");
    }

    private void readCmdError() {
        try {
            print(errorStream);
        } catch (Exception e) {
            throw new RuntimeException("获取错误流异常", e);
        }
        System.out.println("异常输出流结束");
    }

    /**
     * 这里直接把数据打印出来了，有其他需求的可以在这里写判来确定下一步来执行什么。
     */
    private void print(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        int i;
        while (true) {
            i = inputStream.read(bytes);
            if (i == -1) {
                break;
            }
            String str = new String(bytes, 0, i, StandardCharsets.UTF_8);
            if (str.contains("cmake version 3.25.1")) {
                log.info("----------------------callerThread.notify()------------------------");
                synchronized (callerThread) {
                    callerThread.notify();
                }
            }
            log.info(str);
        }
        log.info("\n");
    }

}
