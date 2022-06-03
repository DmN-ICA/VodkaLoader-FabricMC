package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.api.EnvType;
import DmN.ICA.vodka.impl.util.ReflectionHelper;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;

public class VodkaClassLoader extends DmN.ICA.vodka.api.VodkaClassLoader {
    public static final Class<?> KnotClassDelegate;
    public final EnvType envType;
    public final Object delegate;

    public VodkaClassLoader(URL[] urls, ClassLoader parent, EnvType envType) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        super(urls, parent);
        this.envType = envType;
        this.delegate = ReflectionHelper.getField(Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader"), "delegate", parent);
    }

    public static VodkaClassLoader create(File modsDir, ClassLoader parent, EnvType envType) throws MalformedURLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        return new VodkaClassLoader(buildModsDir(modsDir), parent, envType);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            Class<?> clazz = VodkaFindLoadedClass(name);
            System.out.println("[] " + name + " [] " + clazz + " []");
            if (clazz != null)
                return clazz;
            byte[] bytes = this.transform(envType, name, getBytes(name));
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            return super.loadClass(name, resolve);
        }
    }

    public static final MethodHandle KnotClassDelegate$getPostMixinClassByteArray;

    public byte[] getBytes(String name) {
        try {
            return (byte[]) KnotClassDelegate$getPostMixinClassByteArray.invoke(delegate, name, false);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static final MethodHandle ClassLoader$findLoadedClass;

    public Class<?> VodkaFindLoadedClass(String name) {
        Class<?> clazz = this.findLoadedClass(name);
        ClassLoader parent = this.getParent();
        try {
            while (clazz == null && parent != null) {
                clazz = (Class<?>) ClassLoader$findLoadedClass.invoke(parent, name);
                parent = parent.getParent();
            }
        } catch (Throwable ignored) {
        }
        return clazz;
    }

    static {
        try {
            KnotClassDelegate = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            KnotClassDelegate$getPostMixinClassByteArray = ReflectionHelper.IMPL_LOOKUP.findVirtual(KnotClassDelegate, "getPostMixinClassByteArray", MethodType.methodType(byte[].class, String.class, boolean.class));
            ClassLoader$findLoadedClass = ReflectionHelper.IMPL_LOOKUP.findVirtual(ClassLoader.class, "findLoadedClass", MethodType.methodType(Class.class, String.class));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
