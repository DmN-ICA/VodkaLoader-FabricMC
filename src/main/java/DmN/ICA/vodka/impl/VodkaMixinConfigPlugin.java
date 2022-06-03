package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.VodkaLoader;
import DmN.ICA.vodka.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Set;

public class VodkaMixinConfigPlugin implements IMixinConfigPlugin {
    static {
        try {
            ClassLoader parentLoader = VodkaMixinConfigPlugin.class.getClassLoader();
            VodkaClassLoader loader = VodkaClassLoader.create(new File(FabricLoader.getInstance().getGameDir().toString() + File.separator + "vodka_mods"), parentLoader, EnvType.valueOf(FabricLoader.getInstance().getEnvironmentType().toString()));

            Class<TestClass> clazz = (Class<TestClass>) loader.loadClass("DmN.ICA.vodka.impl.TestClass");
            clazz.getMethod("foo").invoke(clazz.newInstance());

            Class.forName("DmN.ICA.vodka.VodkaLoader", true, parentLoader);
            VodkaLoader.INSTANCE = (VodkaLoader) Class.forName("DmN.ICA.vodka.impl.VodkaLoader", true, loader).getConstructors()[0].newInstance(loader);
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
