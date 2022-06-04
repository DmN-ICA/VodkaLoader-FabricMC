package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.api.EnvType;
import DmN.ICA.vodka.impl.util.ReflectionHelper;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class VodkaClassLoader extends DmN.ICA.vodka.api.VodkaClassLoader {
    public static final VodkaClassLoader INSTANCE = null;
    public static final Class<?> KnotClassDelegate;
    public final EnvType envType;
    public final URLClassLoader urlLoader;
    public final ClassLoader knotLoader;
    public final Object delegate;

    public VodkaClassLoader(URL[] urls, ClassLoader parent, ClassLoader knotLoader, EnvType envType) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        super(urls, parent);
        this.knotLoader = knotLoader;
        this.envType = envType;
        Class<?> KnotClassLoader = this.knotLoader.getClass();
        this.urlLoader = ReflectionHelper.getField(KnotClassLoader, "urlLoader", this.knotLoader);
        this.delegate = ReflectionHelper.getField(KnotClassLoader, "delegate", this.knotLoader);
        ReflectionHelper.theUnsafe.putObject(this.knotLoader, ReflectionHelper.ClassLoader$parent, this);
    }

    public static VodkaClassLoader create(File modsDir, ClassLoader parent, ClassLoader knotLoader, EnvType envType) throws MalformedURLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        return new VodkaClassLoader(buildModsDir(modsDir), parent, knotLoader, envType);
    }

    public static final MethodHandle ClassLoader$loadClass;

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("java.") || name.startsWith("net.fabricmc.loader.impl.launch.knot."))
            return ClassLoader.getSystemClassLoader().loadClass(name);
        try {
            Class<?> clazz = VodkaFindLoadedClass(name);
            if (clazz != null)
                return clazz;
            byte[] bytes = this.getTransformedBytes(name, true);
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            try {
                return (Class<?>) ClassLoader$loadClass.invoke(this.urlLoader, name, resolve);
            } catch (Throwable e1) {
                return super.loadClass(name, resolve);
            }
        }
    }

    public static final MethodHandle KnotClassDelegate$getPreMixinClassByteArray;
    public static final VarHandle KnotClassDelegate$transformInitialized;
    public static final MethodHandle KnotClassDelegate$canTransformClass;
    public static final MethodHandle KnotClassDelegate$getMixinTransformer;

    public byte[] getBytes(String name, boolean allowFromParents) {
        try {
            byte[] bytes = (byte[]) KnotClassDelegate$getPreMixinClassByteArray.invoke(delegate, name, allowFromParents);
            if ((boolean) KnotClassDelegate$transformInitialized.get(delegate) || (boolean) KnotClassDelegate$canTransformClass.invoke(name))
                return bytes;
            try {
                return ((IMixinTransformer) KnotClassDelegate$getMixinTransformer.invoke(delegate)).transformClassBytes(name, name, bytes);
            } catch (Throwable t) {
                String msg = String.format("Mixin transformation of %s failed", name);
                Log.warn(LogCategory.KNOT, msg, t);

                throw new RuntimeException(msg, t);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public byte[] getTransformedBytes(String name, boolean allowFromParents) {
        try {
            return transform(envType, name, getBytes(name, allowFromParents));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final MethodHandle ClassLoader$findLoadedClass;

    public Class<?> VodkaFindLoadedClass(String name) {
        Class<?> clazz = null;
        try {
            ClassLoader loader = this.knotLoader;
            while (clazz == null && loader != null) {
                clazz = (Class<?>) ClassLoader$findLoadedClass.invoke(loader, name);
                loader = loader.getParent();
            }
        } catch (Throwable ignored) {
        }
        return clazz;
    }

    static {
        try {
            KnotClassDelegate = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            KnotClassDelegate$getPreMixinClassByteArray = ReflectionHelper.IMPL_LOOKUP.findVirtual(KnotClassDelegate, "getPreMixinClassByteArray", MethodType.methodType(byte[].class, String.class, boolean.class));
            KnotClassDelegate$transformInitialized = ReflectionHelper.IMPL_LOOKUP.unreflectVarHandle(KnotClassDelegate.getDeclaredField("transformInitialized"));
            KnotClassDelegate$canTransformClass = ReflectionHelper.IMPL_LOOKUP.findStatic(KnotClassDelegate, "canTransformClass", MethodType.methodType(boolean.class, String.class));
            KnotClassDelegate$getMixinTransformer = ReflectionHelper.IMPL_LOOKUP.findVirtual(KnotClassDelegate, "getMixinTransformer", MethodType.methodType(IMixinTransformer.class));
            ClassLoader$findLoadedClass = ReflectionHelper.IMPL_LOOKUP.findVirtual(ClassLoader.class, "findLoadedClass", MethodType.methodType(Class.class, String.class));
            ClassLoader$loadClass = ReflectionHelper.IMPL_LOOKUP.findVirtual(ClassLoader.class, "loadClass", MethodType.methodType(Class.class, String.class, boolean.class));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
