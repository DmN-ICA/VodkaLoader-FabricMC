package DmN.ICA.vodka.impl.util;

import DmN.ICA.vodka.impl.VodkaClassLoader;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.util.List;

public class E {
    public static Object env;
    public static MethodHandle VodkaClassLoader$VodkaFindLoadedClass;
    public static MethodHandle VodkaClassLoader$getBytes;
    public static MethodHandle VodkaClassLoader$VodkaGetResource;
    public static MethodHandle VodkaClassLoader$transform;
    public static List<String> VodkaClassLoader$transformed;

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
        try {
            VodkaClassLoader$transformed.add(name);
            if (name.startsWith("javassist/"))
                return bytes;
            return (byte[]) VodkaClassLoader$transform.invoke(env, name, bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void cinit(Object VodkaClassLoader$INSTANCE, ClassLoader loader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        MethodHandles.Lookup lookup = (MethodHandles.Lookup) loader.loadClass("DmN.ICA.vodka.impl.util.ReflectionHelper").getField("IMPL_LOOKUP").get(null);
        Class<VodkaClassLoader> VodkaClassLoader = (Class<DmN.ICA.vodka.impl.VodkaClassLoader>) loader.loadClass("DmN.ICA.vodka.impl.VodkaClassLoader");
        VodkaClassLoader$VodkaFindLoadedClass = lookup.findVirtual(VodkaClassLoader, "VodkaFindLoadedClass", MethodType.methodType(Class.class, String.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$getBytes = lookup.findVirtual(VodkaClassLoader, "getBytes", MethodType.methodType(byte[].class, String.class, boolean.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$VodkaGetResource = lookup.findVirtual(VodkaClassLoader, "VodkaGetResource", MethodType.methodType(URL.class, String.class)).bindTo(VodkaClassLoader$INSTANCE);
        VodkaClassLoader$transform = lookup.findVirtual(loader.loadClass("DmN.ICA.vodka.api.VodkaClassLoader"), "transform", MethodType.methodType(byte[].class, loader.loadClass("DmN.ICA.vodka.api.EnvType"),  String.class, byte[].class)).bindTo(VodkaClassLoader$INSTANCE);
        env = VodkaClassLoader.getField("envType").get(VodkaClassLoader$INSTANCE);
    }
}
