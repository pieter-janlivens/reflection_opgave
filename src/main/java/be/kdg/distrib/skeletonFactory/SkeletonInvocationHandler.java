package be.kdg.distrib.skeletonFactory;

import be.kdg.distrib.communication.MessageManager;
import be.kdg.distrib.communication.MethodCallMessage;

import java.lang.reflect.*;
import java.util.LinkedList;

public class SkeletonInvocationHandler implements InvocationHandler {

    private Object implementation;
    private MessageManager messageManager;

    public SkeletonInvocationHandler(Object implementation) {
        this.implementation=implementation;
        this.messageManager = new MessageManager();
    }


    public MethodCallMessage reply(Object toReply,MethodCallMessage message,String paramName) throws IllegalAccessException {

        if ((toReply instanceof String) || (toReply instanceof Byte)
                || (toReply instanceof Short) || (toReply instanceof Integer)
                || (toReply instanceof Long) || (toReply instanceof Float)
                || (toReply instanceof Double) || (toReply instanceof Boolean)
                || (toReply instanceof Character)){
            message.setParameter(paramName,toReply.toString());
        }
        else {
            Field[] fields = toReply.getClass().getDeclaredFields();
            for(int i =0;i< fields.length;i++){
                fields[i].setAccessible(true);

                reply(fields[i].get(toReply),message,paramName+"."+fields[i].getName());

            }
        }
        return message;
    }

    public Object parse(Class returnType,String value)throws Exception{
        if(returnType.equals(int.class)){
            return Integer.parseInt(value);
        }
        else if (returnType.equals(double.class)){
            return Double.parseDouble(value);
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


    public void handleRequest(MethodCallMessage requestMessage) throws Throwable {

        String requestName = requestMessage.getMethodName();
        Object toReply = null;
        Method[] m = implementation.getClass().getMethods();

        LinkedList<Class> paramTypes = new LinkedList<>();
        LinkedList<Object> args = new LinkedList<>();

        int checkArgNumber = 0;
        for(int x=0;x<m.length;x++){
            if(m[x].getName().equals(requestName)){
                Parameter[] params = m[x].getParameters();
                for(int y=0;y<m[x].getParameterCount();y++){
                    if(!(params[y].getType().isPrimitive()||params[y].getType().equals(String.class))){

                        Object objParam = params[y].getType().getDeclaredConstructor().newInstance();

                        int check=0;
                        for(int i=0;i<requestMessage.getParameters().size();i++){
                            for(Field field:params[y].getType().getDeclaredFields()){
                                if(requestMessage.getParameter("arg"+i+"."+field.getName())!=null){
                                    field.setAccessible(true);
                                    field.set(objParam,parse(field.getType(),requestMessage.getParameter("arg"+i+"."+field.getName())));
                                    check++;
                                    checkArgNumber++;
                                }
                            }
                        }
                        //check if all fields got a value
                        //if(check<)
                        args.add(objParam);
                        paramTypes.add(objParam.getClass());

                    }

                    if(requestMessage.getParameters().containsKey(params[y].getName())){

                        args.add(parse(params[y].getType(),requestMessage.getParameter(params[y].getName())));//args[y]=parse(params[y].getType(),requestMessage.getParameter(params[y].getName()));
                        paramTypes.add(params[y].getType());//paramTypes[y]=params[y].getType();
                        checkArgNumber++;
                    }
                }
            }
        }
        if(checkArgNumber!=requestMessage.getParameters().size()){
            throw new IllegalArgumentException("Wrong amount of arguments.");
        }



        Class[] t = new Class[paramTypes.size()];

        //return from the invoked methode.
        toReply = implementation.getClass().getMethod(requestName,paramTypes.toArray(t)).invoke(implementation,args.toArray());


        MethodCallMessage reply = new MethodCallMessage(messageManager.getMyAddress(),"result");
        reply.setParameter("result","Ok");
        if(toReply != null){
            this.reply(toReply,reply,"result");

        }
        messageManager.send(reply,requestMessage.getOriginator());
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();


        if (methodName == "run") {


            Thread thread = new Thread() {
                public void run() {
                    MethodCallMessage request = messageManager.wReceive();



                    try {
                        handleRequest(request);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    this.run();

                }
            };

            thread.start();
        }
        if (methodName == "getAddress") {
            return messageManager.getMyAddress();
        }
        if (methodName == "handleRequest" && args[0].getClass() == MethodCallMessage.class) {

            handleRequest((MethodCallMessage) args[0]);

        }

        return null;
    }
}
