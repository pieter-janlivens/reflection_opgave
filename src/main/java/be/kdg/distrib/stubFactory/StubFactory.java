package be.kdg.distrib.stubFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class StubFactory {



    public static Object createStub(Class klasse, String ip, int port){



        InvocationHandler handler = new StubInvocationHandler(ip,port);
        Object stub = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{klasse}, handler);



        return stub;

    }


}
