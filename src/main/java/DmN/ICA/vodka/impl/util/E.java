package DmN.ICA.vodka.impl.util;

import java.lang.reflect.InvocationTargetException;

public class E {
    public static byte[] e(ClassLoader loader, String name, boolean arg1) {
        try {
            Class<?> clazz = loader.loadClass("DmN.ICA.vodka.impl.VodkaClassLoader");
            Object cl = clazz.getField("INSTANCE").get(null);
            Class<?> c = (Class<?>) clazz.getMethod("VodkaFindLoadedClass", String.class).invoke(cl, name);
            if (c != null && !c.getName().equals("DmN.ICA.vodka.impl.util.E")) {
                System.out.println("[] " + name + " [] " + c +" []");
                new Exception("ёмаё").printStackTrace();
                System.exit(0);
            }
            return (byte[]) clazz.getMethod("getTransformedBytes", String.class, boolean.class).invoke(cl, name, arg1);
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> e0(ClassLoader loader, String name)  {
        try {
            Class<?> clazz = loader.loadClass("DmN.ICA.vodka.impl.VodkaClassLoader");
            return (Class<?>) clazz.getMethod("VodkaFindLoadedClass", String.class).invoke(clazz.getField("INSTANCE").get(null), name);
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
