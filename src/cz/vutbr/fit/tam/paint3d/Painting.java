package cz.vutbr.fit.tam.paint3d;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Painting {

	public static final String TAG = "3Dpaint|Painting";

	public static final String TABLE_NAME = "painting";

	public static final String[] TABLE_COLUMNS = { "name", "created", "painting_id" };

	private Context context;

	public Integer paintingId;

	public String name;

	public String created;

	public PaintingPointSet paintingPointSet;

	public Painting(Context context) {
		this.context = context;
		this.paintingId = 0;
		this.paintingPointSet = new PaintingPointSet();
	}

	public Painting loadFromCursor(Cursor c, Boolean withPoints) {
		this.paintingId = c.getInt(c.getColumnIndex("painting_id"));
		this.name = c.getString(c.getColumnIndex("name"));
		this.created = c.getString(c.getColumnIndex("created"));

		if (withPoints) {
			SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
			Cursor points = db.query(PaintingPoint.TABLE_NAME, PaintingPoint.TABLE_COLUMNS, "painting_id = ?",
					new String[] { this.paintingId.toString() }, null, null, "painting_point_id");
			if (points.getCount() > 0) {
				while (points.moveToNext()) {
					this.paintingPointSet.add(new PaintingPoint(Float.valueOf(points.getFloat(points
							.getColumnIndex("x"))), Float.valueOf(points.getFloat(points.getColumnIndex("y"))), Float
							.valueOf(points.getFloat(points.getColumnIndex("z")))));
				}
			}
			points.close();
			db.close();
		}
		return this;
	}

	public Painting save() {
		SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
		ContentValues values;
		if (this.paintingId == 0) {
			values = new ContentValues();
			values.put("name", this.name);
			values.put("created", this.created);
			this.paintingId = (int) db.insert(Painting.TABLE_NAME, null, values);

			for (PaintingPoint pp : this.paintingPointSet) {
				values = new ContentValues();
				values.put("x", pp.x.floatValue());
				values.put("y", pp.y.floatValue());
				values.put("z", pp.z.floatValue());
				values.put("painting_id", this.paintingId);
				db.insert(PaintingPoint.TABLE_NAME, null, values);
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
			result[i + 1] = pp.y.floatValue();
			result[i + 2] = pp.z.floatValue();
			i = i + 3;
		}
		return result;
	}

	public Painting getById(Integer paintingId) {
		SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
		Cursor c = db.query(Painting.TABLE_NAME, Painting.TABLE_COLUMNS, "painting_id = ?", new String[] { paintingId
				.toString() }, null, null, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			this.loadFromCursor(c, true);
		}
		c.close();
		db.close();
		return this;
	}

	public void delete() {
		SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
		db.delete(Painting.TABLE_NAME, "painting_id = ?", new String[] { this.paintingId.toString() });
		db.delete(PaintingPoint.TABLE_NAME, "painting_id = ?", new String[] { this.paintingId.toString() });
		db.close();
	}

	public void exportToObj() {
		// tady si pripravim format dat, udelam si nejaky StringWriter nebo tak.
		FileExporter fe = new FileExporter();
		fe.setFilename(this.name + ".obj");
		String data = "";
		for (int i = 0; i < paintingPointSet.size(); i++) {
			PaintingPoint point = paintingPointSet.get(i);
			data.concat("v " + point.x + " " + point.y + " " + point.z + "\n");
		}
		fe.setData(data);
		fe.export();
	}

	public void exportToCollada() {
		// tady si pripravim format dat, udelam si nejaky StringWriter nebo tak.
		FileExporter fe = new FileExporter();
		fe.setFilename(this.name + ".dae");
		String data = "";
		String author = "user";
		
		//2011-11-19T16:55:22
		Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = df.format(c.getTime());
        timestamp.replace(" ", "T");

		data.concat("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<COLLADA xmlns=\"http://www.collada.org/2005/11/COLLADASchema\" version=\"1.4.1\">"
				+ "<asset><contributor><author>" + author
				+ "</author><authoring_tool>3Dpaint</authoring_tool></contributor><created>" + timestamp
				+ "</created><modified>" + timestamp
				+ "</modified><unit name=\"meter\" meter=\"1\"/><up_axis>Z_UP</up_axis></asset>"
				+ "<library_geometries><geometry id=\"Plane-mesh\"><mesh><source id=\"Plane-mesh-positions\">"
				+ "<float_array id=\"Plane-mesh-positions-array\" count=\"" + paintingPointSet.size() + "\">");

		for (int i = 0; i < paintingPointSet.size(); i++) {
			PaintingPoint point = paintingPointSet.get(i);
			data.concat(point.x + " " + point.y + " " + point.z + " ");
		}

		data
				.concat("</float_array><technique_common>"
						+ "<accessor source=\"#Plane-mesh-positions-array\" count=\"48\" stride=\"3\">"
						+ "<param name=\"X\" type=\"float\"/><param name=\"Y\" type=\"float\"/>"
						+ "<param name=\"Z\" type=\"float\"/></accessor></technique_common></source>"
						+ "<source id=\"Plane-mesh-normals\"><float_array id=\"Plane-mesh-normals-array\" count=\"0\"/>"
						+ "<technique_common><accessor source=\"#Plane-mesh-normals-array\" count=\"0\" stride=\"3\">"
						+ "<param name=\"X\" type=\"float\"/><param name=\"Y\" type=\"float\"/>"
						+ "<param name=\"Z\" type=\"float\"/></accessor></technique_common></source>"
						+ "<vertices id=\"Plane-mesh-vertices\"><input semantic=\"POSITION\" source=\"#Plane-mesh-positions\"/>"
						+ "</vertices></mesh><extra><technique profile=\"MAYA\"><double_sided>1</double_sided></technique></extra>"
						+ "</geometry></library_geometries><library_visual_scenes><visual_scene id=\"Scene\" name=\"Scene\">"
						+ "<node id=\"Plane\" type=\"NODE\"><translate sid=\"location\">0 0 0</translate>"
						+ "<rotate sid=\"rotationZ\">0 0 1 0</rotate><rotate sid=\"rotationY\">0 1 0 0</rotate>"
						+ "<rotate sid=\"rotationX\">1 0 0 0</rotate><scale sid=\"scale\">1 1 1</scale>"
						+ "<instance_geometry url=\"#Plane-mesh\"/></node><node id=\"Empty\" type=\"NODE\">"
						+ "<translate sid=\"location\">0 0 0</translate><rotate sid=\"rotationZ\">0 0 1 0</rotate>"
						+ "<rotate sid=\"rotationY\">0 1 0 0</rotate><rotate sid=\"rotationX\">1 0 0 0</rotate>"
						+ "<scale sid=\"scale\">1 1 1</scale></node><node id=\"Camera\" type=\"NODE\">"
						+ "<translate sid=\"location\">0 -8 0</translate><rotate sid=\"rotationZ\">0 0 1 0</rotate>"
						+ "<rotate sid=\"rotationY\">0 1 0 0</rotate><rotate sid=\"rotationX\">1 0 0 0</rotate>"
						+ "<scale sid=\"scale\">1 1 1</scale><instance_camera url=\"#Camera-camera\"/></node>"
						+ "</visual_scene></library_visual_scenes><scene><instance_visual_scene url=\"#Scene\"/></scene></COLLADA>");

		fe.setData(data);
		fe.export();
	}
}
