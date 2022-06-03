package DmN.ICA.vodka.world.api;

public interface IClientWorld extends IWorld {
    @Override
    default boolean isClient() {
        return true;
    }
}
