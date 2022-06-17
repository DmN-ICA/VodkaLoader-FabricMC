package DmN.ICA.vodka.entity.api;

import DmN.ICA.vodka.util.Vec3d;
import DmN.ICA.vodka.world.api.IWorld;

public interface IEntity {
    Vec3d getPos();

    void setPos(Vec3d pos);

    IWorld getWorld();
}
