package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.VodkaLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class Initializer implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
//        VodkaLoader.INSTANCE.commonInit();
    }

    @Override
    public void onInitializeClient() {
//        VodkaLoader.INSTANCE.clientInit();
    }

    @Override
    public void onInitializeServer() {
//        VodkaLoader.INSTANCE.serverInit();
    }
}
