package cz.vutbr.fit.tam.paint3d;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class FileExporter {
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

		if ((mExternalStorageAvailable == true) && (mExternalStorageWriteable == true)) {
			try {
				File root = new File(Environment.getExternalStorageDirectory(), filename);
				if (!root.exists()) {
					root.mkdirs();
				}
				File f = new File(root, filename);
				FileWriter writer = new FileWriter(f);
				writer.append(data);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// neda sa zapisovat, malo by to asi vyhodit nejaku hlasku
		}
	}
}
