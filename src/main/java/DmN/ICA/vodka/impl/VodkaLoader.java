package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.api.MinecraftVersion;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.NotNull;

public class VodkaLoader extends DmN.ICA.vodka.VodkaLoader implements ModInitializer {
    @Override
    public void onInitialize() {

    }

    @Override
    public @NotNull MinecraftVersion getMCVersion() {
        return new MinecraftVersion.MinecraftVersionImpl("1.18.2");
    }

    @Override
    public @NotNull LoaderType getLoaderType() {
        return LoaderType.Fabric;
    }
}
