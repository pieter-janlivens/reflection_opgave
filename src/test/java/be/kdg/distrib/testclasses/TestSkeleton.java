package be.kdg.distrib.testclasses;

import be.kdg.distrib.communication.MessageManager;
import be.kdg.distrib.communication.MethodCallMessage;
import be.kdg.distrib.communication.NetworkAddress;

public class TestSkeleton implements Runnable {
    private MessageManager messageManager;
    private MethodCallMessage message;
    private String returnValue;
    private boolean messageReceived;

    public TestSkeleton() {
        this.returnValue = "Ok";
        this.messageManager = new MessageManager();
        this.messageReceived = false;
    }

    public void run() {
        MethodCallMessage message = messageManager.wReceive();
        this.message = message;
        messageReceived = true;
        MethodCallMessage reply = new MethodCallMessage(getMyAddress(), "result");
        if ("object1".equals(returnValue)) {
            reply.setParameter("result.name", "bloop");
            reply.setParameter("result.age", "123");
            reply.setParameter("result.gender", "r");
            reply.setParameter("result.deleted", "true");
        } else if ("object2".equals(returnValue)){
            reply.setParameter("result.name", "blap");
            reply.setParameter("result.age", "534");
            reply.setParameter("result.gender", "O");
            reply.setParameter("result.deleted", "false");
        } else if ("object3".equals(returnValue)) {
            reply.setParameter("result.bla", "qwerty");
            reply.setParameter("result.number", "36");
        } else {
            reply.setParameter("result", returnValue);
        }
        messageManager.send(reply, message.getOriginator());
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean hasMessageBeenReceived() {
        return messageReceived;
    }

    public NetworkAddress getMyAddress() {
        return messageManager.getMyAddress();
    }

    public MethodCallMessage getMessage() {
        return message;
    }

    public void sendStringReturnValue(String s) {
        returnValue = s;
    }

    public void sendIntReturnValue(int i) {
        returnValue = ""+i;
    }

    public void sendCharReturnValue(char c) {
        returnValue = ""+c;
    }

    public void sendBooleanReturnValue(boolean b) {
        returnValue = ""+b;
    }

    public void sendObjectReturnValue(int i) {
        returnValue = "object"+i;
    }
}
