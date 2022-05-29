package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.VodkaLoader;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

public class VodkaMixinConfigPlugin implements IMixinConfigPlugin {
    static {
        try {
            VodkaLoader.INSTANCE = new DmN.ICA.vodka.impl.VodkaLoader(VodkaClassLoader.create(new File(FabricLoader.getInstance().getGameDir().toString() + File.separator + "vodka_mods"), VodkaMixinConfigPlugin.class.getClassLoader()));
            VodkaLoader.INSTANCE.firstInit();
        } catch (MalformedURLException e) {
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
