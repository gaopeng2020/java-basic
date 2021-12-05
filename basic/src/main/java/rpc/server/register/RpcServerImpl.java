package rpc.server.register;

import rpc.server.publication.WeatherImpl;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServerImpl implements RpcServer {
    private HashMap<String, Class> serviceRegister = new HashMap<>();
    private ServerSocket serverSocket = null;
    private int tcpPort = -1;
    public boolean isRunning = false;
    //并发请求，可以使用连接池：连接池中存在多个连接对象，每个连接对象都可以处理一个客户请求。
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //Constructor
    public RpcServerImpl(int port) {
        this.tcpPort = port;
    }

    @Override
    public void start(){
        //服务端打开Socket
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(tcpPort));
            isRunning =true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //监听任意客户端的连接请求，每来一个请求则在线程池中拿一个线程执行
        while (isRunning) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                String clientSoAdInfo = clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort();
                System.out.println("Accept connection to : "+clientSoAdInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //每来一个客户端的请求，则在线程池中拿一个线程执行
            executorService.execute(new Runnable() {
                private Socket socket;

                @Override
                public void run() {
                    ObjectInputStream in = null;
                    ObjectOutputStream out = null;
                    try {
                        //与客户端建立连接之后获取客户端发送过来的数据，并调用相关服务的方法
                        in = new ObjectInputStream(socket.getInputStream());
                        Object returnValue = callServiceMethod(in);

                        //将返回值发送给客户端
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(returnValue);

                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //将clientSocket传递给内名内部类，
                public Runnable setSocket(Socket socket){
                    this.socket = socket;
                    return this;
                }

            }.setSocket(clientSocket));

        }
    }

    private Object callServiceMethod(ObjectInputStream in) {
        try {
            String serviceName = in.readUTF();
            String methodName = in.readUTF();
            Class[] parameterTypes = (Class[]) in.readObject();
            Object[] arguments = (Object[]) in.readObject();

            //服务中心查找客户端请求的服务以及方法，并调用客户端请求的方法
            Class serviceClass = serviceRegister.get(serviceName);
            Method method = serviceClass.getMethod(methodName, parameterTypes);

            Object obj = serviceClass.newInstance();
            if (obj instanceof WeatherImpl){
                WeatherImpl WeatherImpl = (rpc.server.publication.WeatherImpl) obj;
                WeatherImpl.weatherDataUpdate(10.f,1000.f,0.85f);
                System.out.println("serviceClass.getClass() = " + WeatherImpl.getTemperature());
            }

            return method.invoke(serviceClass.newInstance(), arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void stop() {
        isRunning = false;
        executorService.shutdown();
        if (serverSocket!=null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过Map<String serviceName, Class<?>> abstract class>将服务端所有的服务注册到RpcServer,
     */
    @Override
    public void register(Class service, Class<?> serviceImpl) {
        serviceRegister.put(service.getName(), serviceImpl);
    }
}
