package cz.vutbr.fit.tam.paint3d;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class PaintingSet extends ArrayList<Painting> {
    public static final String TAG = "3Dpaint|PaintingSet";
    private Context context;

    public PaintingSet(Context context) {
        this.context = context;
    }


    public PaintingSet getAll() {
        this.clear();
        SQLiteDatabase db = new DatabaseHelper(this.context).getWritableDatabase();
        Cursor c = db.query(Painting.TABLE_NAME, Painting.TABLE_COLUMNS, null, null, null, null, "name");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Painting p = new Painting(this.context);
                p.loadFromCursor(c, false);
                this.add(p);
            }
        }
        c.close();
        db.close();
        return this;
    }
}
