package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.VodkaLoader;
import DmN.ICA.vodka.api.EnvType;
import DmN.ICA.vodka.impl.util.ReflectionHelper;
import javassist.ClassPool;
import javassist.CtClass;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.util.List;
import java.util.Set;

public class VodkaMixinConfigPlugin implements IMixinConfigPlugin {
    static {
        try {
            ClassLoader parentLoader = VodkaMixinConfigPlugin.class.getClassLoader();
            VodkaClassLoader loader = VodkaClassLoader.create(new File(FabricLoader.getInstance().getGameDir().toString() + File.separator + "vodka_mods"), parentLoader.getParent(), parentLoader, EnvType.valueOf(FabricLoader.getInstance().getEnvironmentType().toString()));
            ReflectionHelper.theUnsafe.putObject(VodkaClassLoader.class, ReflectionHelper.theUnsafe.staticFieldOffset(VodkaClassLoader.class.getField("INSTANCE")), loader);

            Class.forName("DmN.ICA.vodka.impl.util.E", true, ClassLoader.getSystemClassLoader()).getMethod("e", ClassLoader.class, String.class, boolean.class).invoke(null, parentLoader, "DmN.ICA.vodka.impl.util.E", true);

            CtClass clazz = ClassPool.getDefault().get("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            clazz.getMethod("getPostMixinClassByteArray", "(Ljava/lang/String;Z)[B").setBody("{ return DmN.ICA.vodka.impl.util.E.e(this.classLoader, $1, $2); }");

            CtClass clazz1 = ClassPool.getDefault().get("net.fabricmc.loader.impl.launch.knot.KnotClassLoader");
            clazz1.getMethod("findLoadedClassFwd", "(Ljava/lang/String;)Ljava/lang/Class;").setBody("{ Class clazz = super.findLoadedClass($1); if (clazz == null) return DmN.ICA.vodka.impl.util.E.e0(this, $1); return clazz; }");

            ByteBuddyAgent.install().redefineClasses(new ClassDefinition(Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader"), clazz1.toBytecode()), new ClassDefinition(Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate"), clazz.toBytecode()));

            Class<TestClass> clazz0 = (Class<TestClass>) parentLoader.loadClass("DmN.ICA.vodka.impl.TestClass");
            clazz0.getMethod("foo").invoke(clazz0.newInstance());

            VodkaLoader.INSTANCE = new DmN.ICA.vodka.impl.VodkaLoader(loader);
            VodkaLoader.INSTANCE.firstInit();
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
