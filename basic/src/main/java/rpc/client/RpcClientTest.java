package rpc.client;

import rpc.client.subscriber.HuaweiWeatherApp;
import rpc.client.subscriber.XiaomiWeatherApp;
import rpc.server.publication.Observer;
import rpc.server.publication.WeatherSubject;
import rpc.server.service.HelloService;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Administrator
 */
public class RpcClientTest {
    public static void main(String[] args){
        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.01" , 6666);
//        rpcMethodCallTest1();
        subscribeTest(serverAddress);

    }

    private static void subscribeTest(InetSocketAddress serverAddress ) {
        String serviceName = "rpc.server.publication.WeatherSubject";
        WeatherSubject weatherSubject= getServiceProxy(serverAddress, serviceName);

        Observer xiaomiWeatherApp = new XiaomiWeatherApp();
        Observer huaweiWeatherApp = new HuaweiWeatherApp();

        assert weatherSubject != null;
        String xiaomiSubscribeAck = weatherSubject.registerObserver(xiaomiWeatherApp);
        System.out.println("xiaomiSubscribeAck = " + xiaomiSubscribeAck);
        String huaweiSubscribeAck = weatherSubject.registerObserver(huaweiWeatherApp);
        System.out.println("huaweiSubscribeAck = " + huaweiSubscribeAck);

    }



    private static void rpcMethodCallTest1(InetSocketAddress serverAddress ) {
        String serviceName = "rpc.server.service.HelloService";
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                HelloService helloService = getServiceProxy(serverAddress, serviceName);
                assert helloService != null;
                String str = helloService.sayHello("gao peng");
                System.out.println("str = " + str);
            }
        },10,1000);
    }

    private static <T> T getServiceProxy(InetSocketAddress serverAddress, String serviceName) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        try {
            return rpcClientProxy.getRemoteProxyServiceObj(Class.forName(serviceName), serverAddress);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
