package be.kdg.distrib;

import be.kdg.distrib.stubFactory.StubFactory;
import be.kdg.distrib.communication.MethodCallMessage;
import be.kdg.distrib.communication.NetworkAddress;
import be.kdg.distrib.testclasses.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestStubFactory {
    private int port;
    private TestSkeleton testSkeleton;

    @Before
    public void setup() {
        testSkeleton = new TestSkeleton();
        testSkeleton.start();
        NetworkAddress address = testSkeleton.getMyAddress();
        port = address.getPortNumber();
    }

    @Test
    public void testCreateClassWithRightMethods() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        stub.testMethod1();
    }

    @Test
    public void testCreateClassFromOtherInterface() {
        TestInterface3 stub = (TestInterface3) StubFactory.createStub(TestInterface3.class, "127.0.0.1", port);
        stub.testMethod(42);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateStubFromObject() {
        StubFactory.createStub(TestObject.class, "127.0.0.1", port);
    }

    @Test
    public void testCreateClassWithDerivedInterface() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface2.class, "127.0.0.1", port);
        stub.testMethod1();
    }

    @Test(expected = java.lang.ClassCastException.class)
    public void testTryToCastToWrongClass() {
        TestInterface stub = (TestInterface2) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        stub.testMethod1();
    }

    @Test(timeout = 1000)
    public void testVoidVoid() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        stub.testMethod1();
        while(!testSkeleton.hasMessageBeenReceived());
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod1", message.getMethodName());
    }

    @Test(timeout = 1000)
    public void testWithOneParam() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        stub.testMethod2("azerty");
        while(!testSkeleton.hasMessageBeenReceived());
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod2", message.getMethodName());
        assertEquals(1, message.getParameters().size());
        assertEquals("azerty", message.getParameter("arg0"));
    }

    @Test(timeout = 1000)
    public void testWithIntParam() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        stub.testMethod9(42);
        while(!testSkeleton.hasMessageBeenReceived());
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod9", message.getMethodName());
        assertEquals(1, message.getParameters().size());
        assertEquals("42", message.getParameter("arg0"));
    }

    @Test(timeout = 1000)
    public void testWithMoreParams() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        stub.testMethod3(42, "forty-two", 3.14, true, 'c');
        while(!testSkeleton.hasMessageBeenReceived());
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod3", message.getMethodName());
        assertEquals(5, message.getParameters().size());
        assertEquals("42", message.getParameter("arg0"));
        assertEquals("forty-two", message.getParameter("arg1"));
        assertEquals("3.14", message.getParameter("arg2"));
        assertEquals("true", message.getParameter("arg3"));
        assertEquals("c", message.getParameter("arg4"));
    }

    @Test(timeout = 1000)
    public void testWithObjectParams() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        TestObject test = new TestObject("forty-two", 42, 'k', false);
        stub.testMethod4(test);
        while(!testSkeleton.hasMessageBeenReceived());
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod4", message.getMethodName());
        assertEquals(4, message.getParameters().size());
        assertEquals("forty-two", message.getParameter("arg0.name"));
        assertEquals("42", message.getParameter("arg0.age"));
        assertEquals("k", message.getParameter("arg0.gender"));
        assertEquals("false", message.getParameter("arg0.deleted"));
    }

    @Test(timeout = 1000)
    public void testWithObjectParams2() {
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        TestObject2 test = new TestObject2("booo", 12);
        stub.testMethod12(test);
        while(!testSkeleton.hasMessageBeenReceived());
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod12", message.getMethodName());
        assertEquals(2, message.getParameters().size());
        assertEquals("booo", message.getParameter("arg0.bla"));
        assertEquals("12", message.getParameter("arg0.number"));
    }

    @Test
    public void testWithStringReturnValue() {
        testSkeleton.sendStringReturnValue("forty-two");
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        String s = stub.testMethod5();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod5", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals("forty-two", s);
    }

    @Test
    public void testWithStringReturnValue2() {
        testSkeleton.sendStringReturnValue("another string");
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        String s = stub.testMethod5();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod5", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals("another string", s);
    }

    @Test
    public void testWithIntReturnValue() {
        testSkeleton.sendIntReturnValue(42);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        int i = stub.testMethod6();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod6", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals(42, i);
    }

    @Test
    public void testWithIntReturnValue2() {
        testSkeleton.sendIntReturnValue(135);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        int i = stub.testMethod6();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod6", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals(135, i);
    }

    @Test
    public void testWithCharReturnValue() {
        testSkeleton.sendCharReturnValue('K');
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        char c = stub.testMethod7();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod7", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals('K', c);
    }

    @Test
    public void testWithCharReturnValue2() {
        testSkeleton.sendCharReturnValue('A');
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        char c = stub.testMethod7();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod7", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals('A', c);
    }

    @Test
    public void testWithBooleanReturnValue() {
        testSkeleton.sendBooleanReturnValue(true);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        boolean b = stub.testMethod8();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod8", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals(true, b);
    }

    @Test
    public void testWithBooleanReturnValue2() {
        testSkeleton.sendBooleanReturnValue(false);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        boolean b = stub.testMethod8();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod8", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals(false, b);
    }

    @Test
    public void testWithObjectReturnValue() {
        testSkeleton.sendObjectReturnValue(1);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        TestObject test = stub.testMethod11();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod11", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals('r', test.getGender());
        assertEquals("bloop", test.getName());
        assertEquals(123, test.getAge());
        assertTrue(test.isDeleted());
    }

    @Test
    public void testWithObjectReturnValue2() {
        testSkeleton.sendObjectReturnValue(2);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        TestObject test = stub.testMethod11();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod11", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals('O', test.getGender());
        assertEquals("blap", test.getName());
        assertEquals(534, test.getAge());
        assertFalse(test.isDeleted());
    }

    @Test
    public void testWithObjectReturnValue3() {
        testSkeleton.sendObjectReturnValue(3);
        TestInterface stub = (TestInterface) StubFactory.createStub(TestInterface.class, "127.0.0.1", port);
        TestObject2 test = stub.testMethod13();
        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("testMethod13", message.getMethodName());
        assertEquals(0, message.getParameters().size());
        assertEquals("qwerty", test.getBla());
        assertEquals(36, test.getNumber());
    }

    @Test
    public void testWithEverything() {
        testSkeleton.sendObjectReturnValue(1);
        TestInterface2 stub = (TestInterface2) StubFactory.createStub(TestInterface2.class, "127.0.0.1", port);
        TestObject test = new TestObject("blah", 9, 'a', true);
        TestObject result = stub.fullBlownTestMethod("haha", test, 7, false);

        MethodCallMessage message = testSkeleton.getMessage();
        assertEquals("fullBlownTestMethod", message.getMethodName());
        assertEquals(7, message.getParameters().size());
        assertEquals("haha", message.getParameter("arg0"));
        assertEquals("blah", message.getParameter("arg1.name"));
        assertEquals("9", message.getParameter("arg1.age"));
        assertEquals("a", message.getParameter("arg1.gender"));
        assertEquals("true", message.getParameter("arg1.deleted"));
        assertEquals("7", message.getParameter("arg2"));
        assertEquals("false", message.getParameter("arg3"));

        assertEquals("bloop", result.getName());
        assertEquals('r', result.getGender());
        assertEquals(123, result.getAge());
        assertTrue(result.isDeleted());
    }
}
