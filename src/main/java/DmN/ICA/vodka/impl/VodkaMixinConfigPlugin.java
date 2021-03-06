package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.VodkaLoader;
import DmN.ICA.vodka.api.EnvType;
import DmN.ICA.vodka.impl.loader.CacheClassLoader;
import DmN.ICA.vodka.impl.loader.VodkaClassLoader;
import DmN.ICA.vodka.impl.test.TestClass;
import DmN.ICA.vodka.impl.util.E;
import javassist.ClassPool;
import javassist.CtClass;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.util.List;
import java.util.Set;

public class VodkaMixinConfigPlugin implements IMixinConfigPlugin {
    public static final Logger LOGGER = LogManager.getLogger("Vodka[Loader/Api][Inject]");
    public static boolean allowCacheLoader = false;

    static {
        try {
            var env = EnvType.valueOf(FabricLoader.getInstance().getEnvironmentType().toString());
            var parentLoader = VodkaMixinConfigPlugin.class.getClassLoader();
            var pool = ClassPool.getDefault();

            var gameDIr = FabricLoader.getInstance().getGameDir().toString() + File.separator;
            var mods = new File(gameDIr + "vodka_mods");
            var loader = allowCacheLoader ? CacheClassLoader.create(mods, VodkaClassLoader.systemLoader, parentLoader, env, gameDIr + "vodka_cache") : VodkaClassLoader.create(mods, VodkaClassLoader.systemLoader, parentLoader, env);

            var clazz2 = (Class<E>) Class.forName("DmN.ICA.vodka.impl.util.E", true, ClassLoader.getSystemClassLoader());
            clazz2.getMethod("cinit", Object.class, ClassLoader.class).invoke(null, loader, parentLoader);

            clazz2.getMethod("e", String.class, boolean.class).invoke(null, "DmN.ICA.vodka.impl.util.E", true);

            loader.transform(env,  "DmN.ICA.vodka.impl.test.TestClass", pool.get("DmN.ICA.vodka.impl.test.TestClass").toBytecode());

            CtClass clazz = pool.get("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            clazz.getMethod("getPostMixinClassByteArray", "(Ljava/lang/String;Z)[B").setBody("{return DmN.ICA.vodka.impl.util.E.e($1, $2);}");
            pool.get(E.class.getName()).getMethod("e", "(Ljava/lang/String;Z)[B");
            clazz.getMethod("getRawClassByteArray", "(Ljava/lang/String;Z)[B").setBody("""
            {
            $1 = net.fabricmc.loader.impl.util.LoaderUtil.getClassFileName($1);
            java.net.URL url = ((net.fabricmc.loader.impl.launch.knot.KnotClassDelegate.ClassLoaderAccess) classLoader).findResourceFwd($1);
    
            if (url == null) {
                if (!$2) return null;
    
                url = parentClassLoader.getResource($1);
    
                if (!isValidParentUrl(url, $1)) {
                   //if (LOG_CLASS_LOAD) net.fabricmc.loader.impl.util.log.Log.info(net.fabricmc.loader.impl.util.log.LogCategory.KNOT, "refusing to load class %s at %s from parent class loader", $1, getCodeSource(url, $1));
    
                    return null;
                }
            }
    
            java.io.InputStream inputStream = url.openStream();
            int a = inputStream.available();
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream(a < 32 ? 32768 : a);
            byte[] buffer = new byte[8192];
            int len;
    
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
    
            byte[] bytes = outputStream.toByteArray();
            inputStream.close();
            return DmN.ICA.vodka.impl.util.E.e2($1, bytes);
            }""");

            var clazz1 = pool.get("net.fabricmc.loader.impl.launch.knot.KnotClassLoader");
            clazz1.getMethod("findLoadedClassFwd", "(Ljava/lang/String;)Ljava/lang/Class;").setBody("{return DmN.ICA.vodka.impl.util.E.e0($1);}");
            clazz1.getMethod("findResourceFwd", "(Ljava/lang/String;)Ljava/net/URL;").setBody("{return DmN.ICA.vodka.impl.util.E.e3($1);}");
            clazz1.getMethod("getResource", "(Ljava/lang/String;)Ljava/net/URL;").setBody("{return DmN.ICA.vodka.impl.util.E.e1($1);}");
            clazz1.getMethod("getResourceAsStream", "(Ljava/lang/String;)Ljava/io/InputStream;").setBody("{return DmN.ICA.vodka.impl.util.E.e4($1);}");

            ByteBuddyAgent.install().redefineClasses(
                    new ClassDefinition(Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader"), clazz1.toBytecode()),
                    new ClassDefinition(Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate"), clazz.toBytecode())
            );

            LOGGER.info("#" + env + "# " + new TestClass().foo(5, 3));

            (VodkaLoader.INSTANCE = new DmN.ICA.vodka.impl.VodkaLoader(loader)).firstInit();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
