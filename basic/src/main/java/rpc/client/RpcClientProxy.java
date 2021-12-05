package rpc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Administrator
 */
public class RpcClientProxy {
    /**
     * 获取代表服务端接口的动态代理对象，该对象可以是注册到RPC服务端的任何对象
     * serviceInterface：在客户端被代理的服务接口
     * /soAd：待请求服务端的ip：端口
     */
    public <T> T  getRemoteProxyServiceObj(Class<?> serviceInterface, InetSocketAddress soAd) {

        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class[]{serviceInterface},
                (proxy, method, args) -> {
                    Socket socket = null;
                    ObjectOutputStream out = null;
                    ObjectInputStream in = null;
                    try {
                        //与服务端建立TCP连接
                        socket = new Socket();
                        socket.connect(soAd);

                        //发送数据到服务端, 使用序列化流，在对象发出去之前先做序列化
                        out = new ObjectOutputStream(socket.getOutputStream());
                        //发送服务名称，方法名称，使用writeUTF
//                    out.writeUTF(proxy.getClass().getName());
                        out.writeUTF(serviceInterface.getName());
                        out.writeUTF(method.getName());
                        //发送方法的参数类型，以及参数名称
                        out.writeObject(method.getParameterTypes());
                        out.writeObject(args);

                        //获取服务端返回值
                        in = new ObjectInputStream(socket.getInputStream());
                        return in.readObject();

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();

                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    }
                    return null;
                });
    }

}
