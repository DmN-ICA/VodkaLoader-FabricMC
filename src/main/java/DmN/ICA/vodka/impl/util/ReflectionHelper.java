package DmN.ICA.vodka.impl.util;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Please be good and never touch this outside gudASM.
 * */
@SuppressWarnings("all")
public class ReflectionHelper {
    public static final Unsafe theUnsafe = (Unsafe) getUnsafe();
    public static final long AccessibleObject$override = findOverride();

    public static final MethodHandles.Lookup IMPL_LOOKUP = forceGetField(
            MethodHandles.Lookup.class,
            null,
            Modifier.STATIC | Modifier.FINAL,
            MethodHandles.Lookup.class
    );

    public static <O, T> T forceGetField(Class<O> owner, O instance, int mods, Class<T> type) {
        for (Field field : owner.getDeclaredFields()) {
            if (
                    field.getModifiers() == mods &&
                            field.getType() == type
            ) {
                try {
                    forceSetAccessible(field, true);
                    return (T) field.get(instance);
                } catch (ReflectiveOperationException ignored) {
                }
            }
        }
        throw new RuntimeException(String.format(
                "Failed to get field from %s of type %s",
                owner.getName(),
                type.getName()
        ));
    }

    public static long findOverride() {
        try {
            AccessibleObject object = (AccessibleObject) theUnsafe.allocateInstance(AccessibleObject.class);
            for (long cookie = 0; cookie < 64; cookie += 4) {
                int original = theUnsafe.getInt(object, cookie);
                object.setAccessible(true);
                if (original != theUnsafe.getInt(object, cookie)) {
                    theUnsafe.putInt(object, cookie, original);
                    if (!object.isAccessible()) {
                        return cookie;
                    }
                }
                object.setAccessible(false);
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void forceSetAccessible(AccessibleObject object, boolean accessible) {
        theUnsafe.putInt(object, AccessibleObject$override, accessible ? 1 : 0);
    }

    public static MethodHandle findGetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findGetter(owner, name, type);
    }

    public static <O, T extends O> MethodHandle findGetter(Class<T> owner, O instance, String name, Class<?> type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findGetter(owner, name, type).bindTo(instance);
    }

    public static MethodHandle findStaticGetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findStaticGetter(owner, name, type);
    }

    public static MethodHandle findSetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findSetter(owner, name, type);
    }

    public static <O, T extends O> MethodHandle findSetter(Class<T> owner, O instance, String name, Class<?> type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findSetter(owner, name, type).bindTo(instance);
    }

    public static MethodHandle findStaticSetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findStaticSetter(owner, name, type);
    }

    public static <T> Class<T> loadClass(String name) throws ReflectiveOperationException {
        return loadClass(ReflectionHelper.class.getClassLoader(), name);
    }

    public static <T> Class<T> loadClass(ClassLoader loader, String name) throws ReflectiveOperationException {
        return (Class<T>) loader.loadClass(name);
    }

    public static <O, T extends O> MethodHandle findVirtual(Class<T> owner, O instance, String name, MethodType type) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findVirtual(owner, name, type).bindTo(instance);
    }

    public static MethodHandle findStatic(Class<?> owner, String name, Class<?> returnType, Class<?>... params) throws ReflectiveOperationException {
        return IMPL_LOOKUP.findStatic(
                owner,
                name,
                MethodType.methodType(returnType, params)
        );
    }

    public static <T> T getField(Class<?> clazz, String name, Object instance) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField(name);
        forceSetAccessible(f, true);
        return (T) f.get(instance);
    }

    public static void setField(Class<?> clazz, String name, Object instance, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField(name);
        forceSetAccessible(f, true);
        f.set(instance, value);
    }

    public static Object getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
