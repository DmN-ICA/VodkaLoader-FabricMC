package DmN.ICA.vodka.util;

public class Vec3d {
    public double x;
    public double y;
    public double z;

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(BlockPos pos) {
        this.x = pos.x + 0.4;
        this.y = pos.y + 0.4;
        this.z = pos.y + 0.4;
    }
}
