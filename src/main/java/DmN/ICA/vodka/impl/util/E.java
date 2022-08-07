package DmN.ICA.vodka.impl.util;

import DmN.ICA.vodka.impl.loader.VodkaClassLoader;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;

public class E {
    public static MethodHandle VodkaClassLoader$VodkaFindLoadedClass;
    public static MethodHandle VodkaClassLoader$getBytes;
    public static MethodHandle VodkaClassLoader$VodkaGetResource;
    public static MethodHandle VodkaClassLoader$transform;
    public static MethodHandle VodkaClassLoader$VodkaFindResource;
    public static MethodHandle VodkaClassLoader$VodkaGetResourceAsStream;

    public static byte[] e(String name, boolean arg1) {
        try {
            return (byte[]) VodkaClassLoader$getBytes.invoke(name, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> e0(String name) {
        try {
            return (Class<?>) VodkaClassLoader$VodkaFindLoadedClass.invoke(name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static URL e1(String name) {
        try {
            return (URL) VodkaClassLoader$VodkaGetResource.invoke(name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] e2(String name, byte[] bytes) {
        if (name.startsWith("javassist/"))
            return bytes;
        try {
            return (byte[]) VodkaClassLoader$transform.invoke(name, bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static URL e3(String name) {
        try {
            return (URL) VodkaClassLoader$VodkaFindResource.invoke(name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream e4(String name) {
        try {
            return (InputStream) VodkaClassLoader$VodkaGetResourceAsStream.invoke(name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void cinit(Object VodkaClassLoader$INSTANCE, ClassLoader loader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        MethodHandles.Lookup lookup = (MethodHandles.Lookup) loader.loadClass("DmN.ICA.vodka.impl.util.ReflectionHelper").getField("IMPL_LOOKUP").get(null);
        Class<VodkaClassLoader> VodkaClassLoader = (Class<DmN.ICA.vodka.impl.loader.VodkaClassLoader>) loader.loadClass("DmN.ICA.vodka.impl.loader.VodkaClassLoader");
        VodkaClassLoader$VodkaFindLoadedClass = lookup.findVirtual(VodkaClassLoader, "VodkaFindLoadedClass", MethodType.methodType(Class.class, String.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$getBytes = lookup.findVirtual(VodkaClassLoader, "getBytes", MethodType.methodType(byte[].class, String.class, boolean.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$VodkaGetResource = lookup.findVirtual(VodkaClassLoader, "VodkaGetResource", MethodType.methodType(URL.class, String.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$transform = MethodHandles.insertArguments(lookup.findVirtual(loader.loadClass("DmN.ICA.vodka.classloader.VodkaClassLoader"), "transform", MethodType.methodType(byte[].class, loader.loadClass("DmN.ICA.vodka.api.EnvType"),  String.class, byte[].class)).bindTo(VodkaClassLoader$INSTANCE), 0, VodkaClassLoader.getField("envType").get(VodkaClassLoader$INSTANCE));
        VodkaClassLoader$VodkaFindResource = lookup.findVirtual(VodkaClassLoader, "VodkaFindResource", MethodType.methodType(URL.class, String.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$VodkaGetResourceAsStream = lookup.findVirtual(VodkaClassLoader, "VodkaGetResourceAsStream", MethodType.methodType(InputStream.class, String.class)).bindTo(VodkaClassLoader$INSTANCE);
    }
}
