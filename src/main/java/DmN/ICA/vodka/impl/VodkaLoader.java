package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.api.MinecraftVersion;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public class VodkaLoader extends DmN.ICA.vodka.VodkaLoader {
    public VodkaLoader(VodkaClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public @NotNull MinecraftVersion getMCVersion() {
        return new MinecraftVersion.MinecraftVersionImpl("1.18.2");
    }

    @Override
    public @NotNull LoaderType getLoaderType() {
        return LoaderType.Fabric;
    }

    @Override
    public @NotNull EnvType getEnvironment() {
        return EnvType.valueOf(FabricLoader.getInstance().getEnvironmentType().name());
    }

    @Override
    public @NotNull Path getModsDir() {
        return new File(FabricLoader.getInstance().getGameDir().toString() + File.separator + "vodka_mods").toPath();
    }

    @Override
    public @NotNull Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
