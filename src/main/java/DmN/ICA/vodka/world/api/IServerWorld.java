package DmN.ICA.vodka.world.api;

public interface IServerWorld extends IWorld {
    @Override
    default boolean isClient() {
        return false;
    }
}
