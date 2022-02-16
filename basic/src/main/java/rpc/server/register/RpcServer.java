package rpc.server.register;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface RpcServer {
    void start() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException;
    void stop();
    void register(Class service, Class<?> abstractClass);
}
