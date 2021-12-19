package rpc.server.register;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface RpcServer {
    public void start() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException;
    public void stop();
    public void register(Class service, Class<?> abstractClass);
}
