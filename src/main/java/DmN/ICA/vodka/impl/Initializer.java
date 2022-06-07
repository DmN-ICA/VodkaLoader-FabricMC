package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.VodkaLoader;
import net.fabricmc.api.*;

public class Initializer implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        VodkaLoader.INSTANCE.commonInit();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        VodkaLoader.INSTANCE.clientInit();
    }

    @Environment(EnvType.SERVER)
    @Override
    public void onInitializeServer() {
        VodkaLoader.INSTANCE.serverInit();
    }
}
