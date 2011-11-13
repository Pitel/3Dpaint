package cz.vutbr.fit.tam.paint3d;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Painting {
    public static final String TAG = "3Dpaint|Painting";
    private Context context;
    public Integer paintingId;
    public String name;
    public String created;
    public PaintingPointSet paintingPointSet;

    public static final String TABLE_NAME = "painting";
    public static final String[] TABLE_COLUMNS = {"name", "created", "painting_id"};

    public Painting(Context context) {
        this.context = context;
        this.paintingId = 0;
        this.paintingPointSet = new PaintingPointSet();
    }

    public Painting loadFromCursor(Cursor c) {
        this.paintingId = c.getInt(c.getColumnIndex("painting_id"));
        this.name = c.getString(c.getColumnIndex("name"));
        this.created = c.getString(c.getColumnIndex("created"));

        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        Cursor points = db.query("painting_point", new String[]{"x", "y", "z", "painting_point_id", "painting_id"}, "painting_id = ?", new String[]{this.paintingId.toString()}, null, null, "painting_point_id");
        Log.d("Painting", "count:" + c.getCount());
        if (points.getCount() > 0) {
            while(points.moveToNext()) {
                Log.d("Painting", "paintingPointId:" + points.getInt(points.getColumnIndex("painting_point_id")));
                this.paintingPointSet.add(
                    new PaintingPoint(
                        Float.valueOf(points.getFloat(points.getColumnIndex("x"))),
                        Float.valueOf(points.getFloat(points.getColumnIndex("y"))),
                        Float.valueOf(points.getFloat(points.getColumnIndex("z")))
                    )
                );
            }
        }
        points.close();
        db.close();
        return this;
    }

    public Painting save() {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values;
        if (this.paintingId == 0) {
            values = new ContentValues();
            values.put("name", this.name);
            values.put("created", this.created);
            this.paintingId = (int) db.insert(this.TABLE_NAME, null, values);

            
            for (PaintingPoint pp : this.paintingPointSet) {
                values = new ContentValues();
                values.put("x", pp.x.floatValue());
                values.put("y", pp.y.floatValue());
                values.put("z", pp.z.floatValue());
                values.put("painting_id", this.paintingId);
                db.insert("painting_point", null, values);
            }
        }
        db.close();
        return this;
    }

    float[] getVertices() {
        float[] result = new float[this.paintingPointSet.size() * 3];
        int i = 0;
        for (PaintingPoint pp : this.paintingPointSet) {
            result[i] = pp.x.floatValue();
            result[i+1] = pp.y.floatValue();
            result[i+2] = pp.z.floatValue();
            i = i + 3;
        }
        return result;
    }

    public Painting getById(Integer paintingId) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        Cursor c = db.query(Painting.TABLE_NAME, Painting.TABLE_COLUMNS, "painting_id = ?", new String[]{paintingId.toString()}, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            this.loadFromCursor(c);
        }
        c.close();
        db.close();
        return this;
    }

    void delete() {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.delete("painting", "painting_id = ?", new String[]{this.paintingId.toString()});
        db.delete("painting_point", "painting_id = ?", new String[]{this.paintingId.toString()});
    }
}
