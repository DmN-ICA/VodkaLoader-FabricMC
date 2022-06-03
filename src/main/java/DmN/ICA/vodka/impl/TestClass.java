package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.annotations.EnvironmentMethod;

public class TestClass {
    @EnvironmentMethod(client = "fooC()V", server = "fooS()V")
    public void foo() {
        System.out.println("Foo!");
    }

    public void fooC() {
        System.out.println("FooClient!");
    }

    public void fooS() {
        System.out.println("FooServer!");
    }
}
