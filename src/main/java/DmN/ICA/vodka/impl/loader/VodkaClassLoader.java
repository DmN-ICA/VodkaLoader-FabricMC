package DmN.ICA.vodka.impl.loader;

import DmN.ICA.vodka.api.EnvType;
import DmN.ICA.vodka.impl.util.ReflectionHelper;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class VodkaClassLoader extends DmN.ICA.vodka.api.VodkaClassLoader {
    public static final Class<?> KnotClassDelegate;
    public final EnvType envType;
    public final net.fabricmc.api.EnvType envTypeF;
    public static final ClassLoader systemLoader = ClassLoader.getPlatformClassLoader();
    public final URLClassLoader urlLoader;
    public final ClassLoader knotLoader;
    public final Object delegate;

    public VodkaClassLoader(URL[] urls, ClassLoader parent, ClassLoader knotLoader, EnvType envType) throws NoSuchFieldException, IllegalAccessException {
        super(urls, parent);
        this.knotLoader = knotLoader;
        this.envType = envType;
        this.envTypeF = net.fabricmc.api.EnvType.valueOf(envType.toString());
        Class<?> KnotClassLoader = this.knotLoader.getClass();
        this.urlLoader = ReflectionHelper.getField(KnotClassLoader, "urlLoader", this.knotLoader);
        this.delegate = ReflectionHelper.getField(KnotClassLoader, "delegate", this.knotLoader);
    }

    public static VodkaClassLoader create(File modsDir, ClassLoader parent, ClassLoader knotLoader, EnvType envType) throws MalformedURLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        return new VodkaClassLoader(buildModsDir(modsDir), parent, knotLoader, envType);
    }

    public static final MethodHandle ClassLoader$loadClass;

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (sysClassCheck(name))
            return Class.forName(name, true, systemLoader);

        try {
            Class<?> clazz = VodkaFindLoadedClass(name);
            if (clazz != null)
                return clazz;
            byte[] bytes = this.getBytes(name, true);
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

    public byte[] getRawBytes(String name, boolean allowFromParents) throws Throwable {
        return  (byte[]) KnotClassDelegate$getPreMixinClassByteArray.invoke(delegate, name, allowFromParents);
    }

    public byte[] getBytes(String name, boolean allowFromParents) {
        try {
            byte[] bytes = getRawBytes(name, allowFromParents);
            if (!(boolean) KnotClassDelegate$transformInitialized.get(delegate) || !(boolean) KnotClassDelegate$canTransformClass.invoke(name))
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

    public static VarHandle KnotClassDelegate$isDevelopment;

    public static final MethodHandle ClassLoader$findLoadedClass;

    public Class<?> VodkaFindLoadedClass(String name) throws ClassNotFoundException {
        if (sysClassCheck(name))
            return Class.forName(name, true, systemLoader);

        Class<?> clazz = this.findLoadedClass(name);
        try {
            ClassLoader loader = this.knotLoader;
            while (clazz == null && loader != null) {
                clazz = (Class<?>) ClassLoader$findLoadedClass.invoke(loader, name);
                loader = loader.getParent();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return clazz;
    }

    @Nullable
    public URL VodkaGetResource(String name) {
        URL resource = urlLoader.getResource(name);
        return resource == null ? this.getResource(name) : resource;
    }

    @Nullable
    public URL VodkaFindResource(String name) {
        URL resource = urlLoader.findResource(name);
        return resource == null ? this.findResource(name) : resource;
    }

    public static boolean sysClassCheck(String name) {
        name = name.split("\\.")[0];
        int length = name.length();
        return length < 4 && (name.equals("jdk") || name.equals("sun")) || name.startsWith("java") && length < 6;
    }

    public Class<?> superLoadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    static {
        try {
            KnotClassDelegate = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            KnotClassDelegate$getPreMixinClassByteArray = ReflectionHelper.IMPL_LOOKUP.findVirtual(KnotClassDelegate, "getPreMixinClassByteArray", MethodType.methodType(byte[].class, String.class, boolean.class));
            KnotClassDelegate$transformInitialized = ReflectionHelper.IMPL_LOOKUP.unreflectVarHandle(KnotClassDelegate.getDeclaredField("transformInitialized"));
            KnotClassDelegate$canTransformClass = ReflectionHelper.IMPL_LOOKUP.findStatic(KnotClassDelegate, "canTransformClass", MethodType.methodType(boolean.class, String.class));
            KnotClassDelegate$getMixinTransformer = ReflectionHelper.IMPL_LOOKUP.findVirtual(KnotClassDelegate, "getMixinTransformer", MethodType.methodType(IMixinTransformer.class));
            KnotClassDelegate$isDevelopment = ReflectionHelper.IMPL_LOOKUP.unreflectVarHandle(KnotClassDelegate.getDeclaredField("isDevelopment"));
            ClassLoader$findLoadedClass = ReflectionHelper.IMPL_LOOKUP.findVirtual(ClassLoader.class, "findLoadedClass", MethodType.methodType(Class.class, String.class));
            ClassLoader$loadClass = ReflectionHelper.IMPL_LOOKUP.findVirtual(ClassLoader.class, "loadClass", MethodType.methodType(Class.class, String.class, boolean.class));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
