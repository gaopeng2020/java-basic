package multhread;

import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author ：xxx
 * @date ：Created in 2021/2/17 23:46
 * @description：
 * @modified By：
 * @version: $
 */
public class WaitNotify {
    //定义一个读取数据的类
    static class Reader extends Thread {
        private final Buffer buffer;

        public Reader(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for (int i = 0; i < buffer.getBufferSize(); i++) {
                int data = buffer.read();
                System.out.println("Read = " + data);
            }
        }
    }

    //定义一个写入数据的类
    static class Writer extends Thread {
        private final Buffer buffer;

        public Writer(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for (int i = 0; i < buffer.getBufferSize(); i++) {
                buffer.write(i);
                System.out.println("Write:" + i);
            }
            super.run();
        }
    }

    //定义一个容器
    static class Buffer {
        private final int bufferSize;
        //用一个队列实现先到的数据先取
        private final Queue<Integer> queue = new LinkedList<>();
        private int count = 0;

        //用户通过构造器决定buffer的大小
        public Buffer(int bufferSize) {
            this.bufferSize = bufferSize;
        }

        public int getBufferSize() {
            return bufferSize;
        }

        //writer 写数据的buffer
        public synchronized void write(int data) {
            //如果buffer满了，则暂停写入数据到buffer，通知reader取数据
            if (count == bufferSize) {
                //暂停写入数据到buffer
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //通知reader取数据
                this.notifyAll();
            }

            //如果buffer没有满，则继续写入数据，并通知Reader可以取数据
            queue.offer(data);
            count++;

            //单buffer>=10个元素时就可以通知了，不用等到buffer满了再让Reader取数据
            if (count >= 10) {
                this.notifyAll();
            }
        }

        //reader从buffer中取数据
        public synchronized int read() {
            //如果buffer的数据数量为0时，则暂停读取数据，并通知Writer写入数据
            if (count <= 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //通知Writer写入数据
                this.notifyAll();
            }

            //如果buffer的数据数量为大于0时，则开始读取数据
            Integer data = queue.poll();
            count--;

            //当buffer小于10个元素时就可以通知了，不用等到buffer为0时再让Reader取数据
            if (count < 10) {
                this.notifyAll();
            }
            return data;
        }
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer(100);
        new Thread(new Reader(buffer), "ReaderThread").start();
        new Thread(new Writer(buffer), "WriterThread").start();
    }
}
