package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.api.MinecraftVersion;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

public class VodkaLoader extends DmN.ICA.vodka.VodkaLoader implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        super.commonInit();
    }

    @Override
    public void onInitializeClient() {
        super.clientInit();
    }

    @Override
    public void onInitializeServer() {
        super.serverInit();
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
}
