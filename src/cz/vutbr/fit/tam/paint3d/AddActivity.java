package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AddActivity extends Activity implements SensorEventListener {
    public static final String TAG = "3Dpaint|AddActivity";
    public ToggleButton button;
    private SensorManager mSensorManager;
    private Painting painting;
    private EditText name;
    private Float x = new Float(0);
    private Float y = new Float(0);
    private Float z = new Float(0);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        button = (ToggleButton) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);

        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddActivity.this, "Vyplňte název kresby!", Toast.LENGTH_LONG).show();
                    button.setChecked(false);
                    return;
                }
                if (button.isChecked()) {
                    x = new Float(0);
                    y = new Float(0);
                    z = new Float(0);
                    painting = new Painting(AddActivity.this);
                } else {
                    painting.name = name.getText().toString();
                    painting.save();
                    Intent intent = new Intent(AddActivity.this, DetailActivity.class);
                    intent.putExtra("paintingId", painting.paintingId);
                    startActivity(intent);
                }
            }
        });
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                if (button.isChecked()) {
                    if (x != (Math.round(event.values[0] * 100) / 100) ||
                        y != (Math.round(event.values[1] * 100) / 100) ||
                        z != (Math.round(event.values[2] * 100) / 100)) {
                            x += Math.round(event.values[0] * 100) / 100;
                            y += Math.round(event.values[1] * 100) / 100;
                            z += Math.round(event.values[2] * 100) / 100;
                            this.painting.paintingPointList.add(new PaintingPoint(x, y, z));
                    }
                }
                break;
            default:
                break;
        }
    }

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
}
