package rpc.server.register;

import sun.net.idn.Punycode;

import javax.sound.midi.VoiceStatus;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.PublicKey;

public interface RpcServer {
    public void start() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException;
    public void stop();
    public void register(Class service, Class<?> abstractClass);
}
