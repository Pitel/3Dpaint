package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PaintActivity extends Activity implements SensorEventListener
{
    public static final String TAG = "3Dpaint|PaintActivity";
    private SensorManager mSensorManager;
    private TextView accelerometer;
    private TextView orientation;
    private TextView magnetic;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = (TextView) findViewById(R.id.accelerometer);
        orientation = (TextView) findViewById(R.id.orientation);
        magnetic = (TextView) findViewById(R.id.magnetic);
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometer.setText("\nAccelerometer:\nx: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2] + "\n");
            break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetic.setText("\nMagnetic Field:\nx: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2] + "\n");
            break;
            case Sensor.TYPE_ORIENTATION:
                orientation.setText("\nOrientation:\nx: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2] + "\n");
            break;
            default:
                Log.d(TAG, "Sensor: " + event.sensor.getName() + ", x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2]);
            break;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }
}
