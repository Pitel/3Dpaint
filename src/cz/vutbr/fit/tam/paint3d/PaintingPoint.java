package cz.vutbr.fit.tam.paint3d;

class PaintingPoint {

    public static final String TAG = "3Dpaint|PaintingPoint";
    public static final String TABLE_NAME = "painting_point";
    public static final String[] TABLE_COLUMNS = {"painting_point_id", "painting_id", "x", "y", "z"};
    public Float x;
    public Float y;
    public Float z;

    public PaintingPoint(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
