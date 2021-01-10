package be.kdg.distrib.stubFactory;

import be.kdg.distrib.communication.MessageManager;
import be.kdg.distrib.communication.MethodCallMessage;
import be.kdg.distrib.communication.NetworkAddress;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StubInvocationHandler implements InvocationHandler {

    private String ip;
    private int port;
    private final MessageManager messageManager;
    private NetworkAddress adress;

    public StubInvocationHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.messageManager = new MessageManager();
        this.adress = new NetworkAddress(ip,port);
    }

    public Object parse(Class returnType,String value){
        if(returnType.equals(int.class)){
            return Integer.parseInt(value);
        }
        else if(returnType.equals(char.class)||value.toCharArray().length<=1){

            return value.charAt(0);
        }
        else if(returnType.equals(boolean.class)){

            return Boolean.parseBoolean(value);
        }
        else if (!returnType.equals(void.class)){
            return returnType.cast(value);
        }
        else{
            return null;
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke() aangeroepen");
        String methodName = method.getName();
        System.out.println("\tmethodName = " + methodName);

        if (args != null) {
            for (Object arg : args) {
                System.out.println("\targ = " + arg);
            }
        }

        MethodCallMessage message = new MethodCallMessage(messageManager.getMyAddress(), method.getName());
        if (args != null) {


            for (int i = 0; i < args.length; i++) {

                if ((args[i] instanceof String) || (args[i] instanceof Byte)
                        || (args[i] instanceof Short) || (args[i] instanceof Integer)
                        || (args[i] instanceof Long) || (args[i] instanceof Float)
                        || (args[i] instanceof Double) || (args[i] instanceof Boolean)
                        || (args[i] instanceof Character)) {
                    message.setParameter("arg" + i, args[i].toString());
                } else {


                    if (args[i].getClass().getDeclaredFields().length != 0) {
                        Class argClass = args[i].getClass();
                        for (int j = 0; j < argClass.getDeclaredFields().length; j++) {


                            Field field = argClass.getDeclaredFields()[j];
                            field.setAccessible(true);
                            message.setParameter("arg" + i + "." + field.getName(), field.get(args[i]).toString());


                        }
                    }
                }
            }
        }
        messageManager.send(message, adress);

        String value = "";
        while ("".equals(value)) {
            MethodCallMessage reply = messageManager.wReceive();
            if (!"result".equals(reply.getMethodName())) {
                continue;
            }

            value = reply.getParameter("result");

            Class returnType = method.getReturnType();


            if (reply.getParameter("result") != null) {
                return parse(returnType, value);
            } else {
                Object toReturn = returnType.getDeclaredConstructor().newInstance();
                for (String param : reply.getParameters().keySet()) {

                    //cut off result.
                    String fieldName = param.substring(7);
                    Field field = returnType.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object fieldValue = parse(field.getType(), reply.getParameter(param));

                    field.set(toReturn, fieldValue);
                }
                return toReturn;
            }



        }
        return null;
    }
}
