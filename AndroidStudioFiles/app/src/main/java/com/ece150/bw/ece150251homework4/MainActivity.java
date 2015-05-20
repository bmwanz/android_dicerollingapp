package com.ece150.bw.ece150251homework4;

import android.app.ActionBar;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends ActionBarActivity implements SensorListener{

    private SensorManager sensorMgr;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;
    private CubeView dieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        boolean accelSupported = sensorMgr.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);

        if (!accelSupported) {
            // on accelerometer on this device
            sensorMgr.unregisterListener(this,
                    SensorManager.SENSOR_ACCELEROMETER);
        }

        dieView = new CubeView(this);
        setContentView(dieView);

        //Button rollButton = (Button)findViewById(R.id.roll_button);
        Button rollButton = new Button(this);
        rollButton.setText("Rolling");
        rollButton.setX(800);
        rollButton.setY(50);
        this.addContentView(rollButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        rollButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dieView.rollDie();
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        dieView.onPause();
        if (sensorMgr != null) {
            sensorMgr.unregisterListener(this,
                    SensorManager.SENSOR_ACCELEROMETER);
            sensorMgr = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dieView.onResume();
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z)/ diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    dieView.rollDie();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {}
}
