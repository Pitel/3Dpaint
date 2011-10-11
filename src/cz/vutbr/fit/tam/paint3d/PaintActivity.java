package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PaintActivity extends Activity implements SensorEventListener {
    public static final String TAG = "3Dpaint|PaintActivity";

    private SensorManager mSensorManager;
    private TextView accelerometer;
    private TextView orientation;
    private TextView magnetic;
    private Button button;
    private Boolean isMeasuring = false;
    private List<Float> vertices = new ArrayList<Float>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = (TextView) findViewById(R.id.accelerometer);
        orientation = (TextView) findViewById(R.id.orientation);
        magnetic = (TextView) findViewById(R.id.magnetic);
        button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isMeasuring) {
                    isMeasuring = false;
                    Intent intent = new Intent(PaintActivity.this, ViewActivity.class);
                    float[] result = new float[vertices.size()];
                    for (int i = 0; i < vertices.size(); i++) {
                        result[i] = vertices.get(i).floatValue();
                    }
                    button.setText("Start 3D paint!");
                    intent.putExtra("vertices", result);
                    startActivity(intent);
                } else {
                    isMeasuring = true;
                    vertices = new ArrayList<Float>();
                    button.setText("Stop 3D paint!");
                }
            }
        });
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (isMeasuring) {
                    vertices.add(event.values[0]);
                    vertices.add(event.values[1]);
                    vertices.add(event.values[2]);
                }
                accelerometer.setText("\nAccelerometer:\nx: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2] + "\n\nPoints: " + vertices.size());
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
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }
}
