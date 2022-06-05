package DmN.ICA.vodka.impl.util;

import DmN.ICA.vodka.impl.VodkaClassLoader;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;

public class E {
    public static Object VodkaClassLoader$INSTANCE;
    public static MethodHandle VodkaClassLoader$VodkaFindLoadedClass;
    public static MethodHandle VodkaClassLoader$getTransformedBytes;
    public static MethodHandle VodkaClassLoader$VodkaGetResource;

    public static byte[] e(String name, boolean arg1) {
        try {
            // DEBUG |>
            Class<?> c = (Class<?>) VodkaClassLoader$VodkaFindLoadedClass.invoke(VodkaClassLoader$INSTANCE, name);
            if (c != null && !c.getName().equals("DmN.ICA.vodka.impl.util.E")) {
                System.out.println(name + " [ERROR] " + c);
                new Exception("ёмаё").printStackTrace();
                System.exit(0);
            } else System.out.println(name + " [OK] " + c);
            // DEBUG <|
            return (byte[]) VodkaClassLoader$getTransformedBytes.invoke(VodkaClassLoader$INSTANCE, name, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> e0(String name) {
        try {
            Class<?> c = (Class<?>) VodkaClassLoader$VodkaFindLoadedClass.invoke(VodkaClassLoader$INSTANCE, name);
            System.out.println(name + " [?] " + c);
            return c;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static URL e1(String name) {
        try {
            return (URL) VodkaClassLoader$VodkaGetResource.invoke(VodkaClassLoader$INSTANCE, name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void cinit(ClassLoader loader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        MethodHandles.Lookup lookup = (MethodHandles.Lookup) loader.loadClass("DmN.ICA.vodka.impl.util.ReflectionHelper").getField("IMPL_LOOKUP").get(null);
        Class<VodkaClassLoader> VodkaClassLoader = (Class<DmN.ICA.vodka.impl.VodkaClassLoader>) loader.loadClass("DmN.ICA.vodka.impl.VodkaClassLoader");
        VodkaClassLoader$VodkaFindLoadedClass = lookup.findVirtual(VodkaClassLoader, "VodkaFindLoadedClass", MethodType.methodType(Class.class, String.class));
        VodkaClassLoader$getTransformedBytes = lookup.findVirtual(VodkaClassLoader, "getTransformedBytes", MethodType.methodType(byte[].class, String.class, boolean.class));
        VodkaClassLoader$VodkaGetResource = lookup.findVirtual(VodkaClassLoader, "VodkaGetResource", MethodType.methodType(URL.class, String.class));
    }
}
