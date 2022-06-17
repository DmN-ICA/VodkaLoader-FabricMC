package DmN.ICA.vodka.impl.test;

import DmN.ICA.vodka.annotations.EnvironmentMethod;

public class TestClass {
    @EnvironmentMethod(client = "fooC(II)Ljava/lang/String;", server = "fooS(II)Ljava/lang/String;")
    public String foo(int a, int b) {
        return "|TestClass|> Foo!";
    }

    public String fooC(int a, int b) {
        return "|TestClass|> a + b = "+ a + " + " + b + " = " + (a + b);
    }

    public String fooS(int a, int b) {
        return "|TestClass|> a & b = " + a + " & " + b + " = "  + (a & b);
    }
}
