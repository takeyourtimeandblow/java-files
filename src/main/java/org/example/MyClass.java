package org.example;

class MyClass {
    @InvokeTimes(2)
    protected void protectedMethod1() {
        System.out.println("Protected Method 1");
    }

    @InvokeTimes(3)
    private void privateMethod1() {
        System.out.println("Private Method 1");
    }

    public void publicMethod() {
        System.out.println("Public Method");
    }

    @InvokeTimes(1)
    protected void protectedMethod2() {
        System.out.println("Protected Method 2");
    }

    private void privateMethod2() {
        System.out.println("Private Method 2");
    }
}