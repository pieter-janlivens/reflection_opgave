package be.kdg.distrib.testclasses;

public class TestImplementation implements TestInterface2 {
    private String s;
    private int i;
    private char c;
    private boolean b;
    private double d;
    private String return5;
    private int return6;
    private char return7;
    private boolean return8;

    @Override
    public void testMethod1() {
        s = "void";
    }

    @Override
    public void testMethod2(String s) {
        this.s = s;
    }

    @Override
    public void testMethod3(int i, String s, double d, boolean b, char c) {
        this.i = i;
        this.s = s;
        this.d = d;
        this.b = b;
        this.c = c;
    }

    @Override
    public void testMethod4(TestObject test) {
        c = test.getGender();
        s = test.getName();
        b = test.isDeleted();
        i = test.getAge();
    }

    public void setMethod5ReturnValue(String v) { this.return5 = v; }

    @Override
    public String testMethod5() {
        return return5;
    }

    public void setMethod6ReturnValue(int v) { this.return6 = v; }

    @Override
    public int testMethod6() {
        return return6;
    }

    public void setMethod7ReturnValue(char v) { this.return7 = v; }

    @Override
    public char testMethod7() {
        return return7;
    }

    public void setMethod8ReturnValue(boolean v) { this.return8 = v; }

    @Override
    public boolean testMethod8() {
        return return8;
    }

    @Override
    public void testMethod9(int i) {
        this.i = i;
    }

    @Override
    public TestObject testMethod11() {
        return new TestObject("hoehoe", 97, 'p', false);
    }

    @Override
    public void testMethod12(TestObject2 o) {
        this.s = o.getBla();
        this.i = o.getNumber();
    }

    @Override
    public TestObject2 testMethod13() {
        return new TestObject2("qwerty", 36);
    }

    @Override
    public TestObject fullBlownTestMethod(String s, TestObject test, int a, boolean b) {
        return new TestObject(s, a, test.getGender(), true);
    }

    public String getS() {
        return s;
    }

    public int getI() {
        return i;
    }

    public char getC() {
        return c;
    }

    public boolean isB() {
        return b;
    }

    public double getD() {
        return d;
    }
}
