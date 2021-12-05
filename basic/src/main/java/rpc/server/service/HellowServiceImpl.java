package rpc.server.service;

public class HellowServiceImpl implements HelloService {
    @Override
    public String sayHello(String str) {
        return "hello " + str;
    }
}
