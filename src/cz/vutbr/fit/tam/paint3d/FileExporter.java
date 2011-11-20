package cz.vutbr.fit.tam.paint3d;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileExporter {
	public static final String TAG = "3Dpaint|FileExporter";

	private String filename;

	private String data;

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void export() {
		// zistenie dostupnosti media, z developer.android.com
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// mozeme citat a zapisovat
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// medium je iba na citanie
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// nieco je zle, nezalezi na tom co, proste sa neda pristupovat k
			// mediu
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		Log.i(TAG, "mExternalStorageAvailable: " + mExternalStorageAvailable + ", mExternalStorageWriteable: "
				+ mExternalStorageWriteable);
		if ((mExternalStorageAvailable == true) && (mExternalStorageWriteable == true)) {
			try {
				// Environment.getDataDirectory();
				File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				root.mkdirs();
				if (root.exists()) {
					File f = new File(root, filename);
					if (!f.exists()) {
						f.createNewFile();
					}
					// File f = new File(root, filename);
					// FileWriter writer = new FileWriter(f);
					FileOutputStream writer = new FileOutputStream(f);
					writer.write(data.getBytes());
					writer.flush();
					writer.close();
				} else {
					Log.e(TAG, "Folder doesn't exist and I failed at creating it.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// neda sa zapisovat, malo by to asi vyhodit nejaku hlasku
		}
	}
}
