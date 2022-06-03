package DmN.ICA.vodka.util;

public class BlockPos {
    public int x;
    public int y;
    public int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(Vec3d pos) {
        this.x = (int) Math.round(pos.x);
        this.y = (int) Math.round(pos.y);
        this.z = (int) Math.round(pos.z);
    }

    public BlockPos add(BlockPos pos) {
        return add(pos.x, pos.y, pos.z);
    }

    public BlockPos add(int x, int y, int z) {
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    public BlockPos mul(BlockPos pos) {
        return mul(pos.x, pos.y, pos.z);
    }

    public BlockPos mul(int x, int y, int z) {
        return new BlockPos(this.x * x, this.y * y, this.z * z);
    }
}
