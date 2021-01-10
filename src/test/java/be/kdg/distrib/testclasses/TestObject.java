package be.kdg.distrib.testclasses;

public class TestObject {
    private String name;
    private int age;
    private char gender;
    private boolean deleted;

    public TestObject() {
    }

    public TestObject(String name, int age, char gender, boolean deleted) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
