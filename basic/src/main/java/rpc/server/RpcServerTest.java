package rpc.server;

import rpc.server.publication.WeatherImpl;
import rpc.server.publication.WeatherSubject;
import rpc.server.register.RpcServer;
import rpc.server.register.RpcServerImpl;
import rpc.server.service.HelloService;
import rpc.server.service.HellowServiceImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

public class RpcServerTest {
    public static void main(String[] args) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        //启动RPC Server
        RpcServer server = new RpcServerImpl(6666);

        //将helloService Interface 及其实现类注册到RpcServer
        server.register(HelloService.class, HellowServiceImpl.class);
        server.register(WeatherSubject.class, WeatherImpl.class);

        server.start();
    }
}
