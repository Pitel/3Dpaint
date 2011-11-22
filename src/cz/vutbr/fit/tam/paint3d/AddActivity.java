package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends Activity implements SensorEventListener {

    public static final String TAG = "3Dpaint|AddActivity";
    public ToggleButton button;
    private SensorManager mSensorManager;
    private Painting painting;
    private EditText name;
    private float vx = 0;
    private float vy = 0;
    private float vz = 0;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private ProgressDialog pd;
    private Boolean isSaving = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        button = (ToggleButton) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setIcon(android.R.drawable.ic_dialog_info);
        pd.setTitle(getText(R.string.painting_save_title));
        pd.setMessage(getText(R.string.painting_save_msg));

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddActivity.this, AddActivity.this.getText(R.string.fill_painting_name), Toast.LENGTH_LONG).show();
                    button.setChecked(false);
                    return;
                }
                if (button.isChecked()) {
                    vx = 0;
                    vy = 0;
                    vz = 0;
                    x = 0;
                    y = 0;
                    z = 0;
                    painting = new Painting(AddActivity.this);
                } else {
                    if (!isSaving) {
                        isSaving = true;
                        painting.name = name.getText().toString();
                        DateFormat dateFormat = new SimpleDateFormat("d. M. y H:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        painting.created = dateFormat.format(cal.getTime());
                        new PaintingSaveAsyncTask().execute();
                    }
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (button.isChecked() && event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            vx += event.values[0];
            vy += event.values[1];
            vz += event.values[2];
            
            x += vx;
            y += vy;
            z += vz;
            
            Log.v(TAG, event.timestamp + ": " + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + " -> " + vx + ", " + vy + ", " + vz + " -> " + x + ", " + y + ", " + z);
            
            if (vx != 0 || vy != 0 || vz != 0) {
                painting.paintingPointSet.add(new PaintingPoint(x, y, z));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    private class PaintingSaveAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                painting.save();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            Intent intent = new Intent(AddActivity.this, DetailActivity.class);
            intent.putExtra("paintingId", painting.paintingId);
            startActivity(intent);
        }
    }
}
