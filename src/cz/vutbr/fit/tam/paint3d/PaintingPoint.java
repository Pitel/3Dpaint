package cz.vutbr.fit.tam.paint3d;

class PaintingPoint {

    public static final String TAG = "3Dpaint|PaintingPoint";
    public static final String TABLE_NAME = "painting_point";
    public static final String[] TABLE_COLUMNS = {"painting_point_id", "painting_id", "x", "y", "z"};
    public int x;
    public int y;
    public int z;

    public PaintingPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
