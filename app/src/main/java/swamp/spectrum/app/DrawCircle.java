package swamp.spectrum.app;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.TextView;

import com.example.caitlin.autismdetector.R;

import java.util.ArrayList;
import java.util.List;

public class DrawCircle extends AppCompatActivity implements SensorEventListener {

    public int animalChoice = 0;

    List<Float> velx = new ArrayList<Float>();
    List<Float> vely = new ArrayList<Float>();
    List<Float> maxvelx = new ArrayList<Float>();
    List<Float> maxvely = new ArrayList<Float>();
    List<Float> accx = new ArrayList<Float>();
    List<Float> accy = new ArrayList<Float>();
    List<Float> accz = new ArrayList<Float>();

    TextView textAvtion;
    TextView textVelocityX;
    TextView textVelocityY;
    TextView textMaxVelocityX;
    TextView textMaxVelocityY;
    TextView textAccelerationX;
    TextView textAccelerationY;
    TextView textAccelerationZ;

    VelocityTracker velocityTracker = null;

    float maxXVelocity;
    float maxYVelocity;

    float maxXAcc = 0;
    float maxYAcc = 0;
    float maxZAcc = 0;

    SensorManager mSensorManager;
    SensorManager sensorManager;
    SensorEventListener _SensorEventListener;
    Sensor mSensor;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_circle);

        Intent intent = getIntent();
        animalChoice = intent.getIntExtra("animalChoice", 0);

        textAvtion = (TextView) findViewById(R.id.action);
        textVelocityX = (TextView) findViewById(R.id.velocityx);
        textVelocityY = (TextView) findViewById(R.id.velocityy);
        textMaxVelocityX = (TextView) findViewById(R.id.maxvelocityx);
        textMaxVelocityY = (TextView) findViewById(R.id.maxvelocityy);
        textAccelerationX = (TextView) findViewById(R.id.accelerationx);
        textAccelerationY = (TextView) findViewById(R.id.accelerationy);
        textAccelerationZ = (TextView) findViewById(R.id.accelerationz);

        textVelocityX.setText("X-velocity (pixel/s): 0");
        textVelocityY.setText("Y-velocity (pixel/s): 0");
        textMaxVelocityX.setText("max. X-velocity: 0");
        textMaxVelocityY.setText("max. Y-velocity: 0");
        textAccelerationX.setText("X-acceleration (pixel/s^2): 0");
        textAccelerationY.setText("Y-acceleration (pixel/s^2): 0");
        textAccelerationZ.setText("Z-acceleration (pixel/s^2): 0");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        listenAccelerometer();
    }

    protected void listenAccelerometer() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fail! we dont have an accelerometer!
        }
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if(x < maxXAcc) {
            maxXAcc = x;
        }
        if(y < maxYAcc) {
            maxYAcc = y;
        }
        if(z < maxZAcc) {
            maxZAcc = z;
        }

        accx.add(x);
        accy.add(y);
        accz.add(z);

        textAccelerationX.setText("X-acceleration (pixel/s^2): " + x);
        textAccelerationY.setText("Y-acceleration (pixel/s^2): " + y);
        textAccelerationZ.setText("Z-acceleration (pixel/s^2): " + z);
    }
    @Override

    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);
                maxXVelocity = 0;
                maxYVelocity = 0;

                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                //1000 provides pixels per second

                float xVelocity = velocityTracker.getXVelocity();
                velx.add(xVelocity);
                float yVelocity = velocityTracker.getYVelocity();
                vely.add(yVelocity);

                if (xVelocity > maxXVelocity) {
                    //max in right side
                    maxXVelocity = xVelocity;
                    maxvelx.add(maxXVelocity);
                }

                if (yVelocity > maxYVelocity) {
                    //Max in down side
                    maxYVelocity = yVelocity;
                    maxvely.add(maxYVelocity);
                }

                textVelocityX.setText("X-velocity (pixel/s): " + xVelocity);
                textVelocityY.setText("Y-velocity (pixel/s): " + yVelocity);
                textMaxVelocityX.setText("max. X-velocity: " + maxXVelocity);
                textMaxVelocityY.setText("max. Y-velocity: " + maxYVelocity);

                break;
            case MotionEvent.ACTION_UP:
                drawAnimal(animalChoice);
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                break;
        }
        return true;
    }

    public void drawAnimal(int animal) {
        Intent intent = new Intent(this, DrawAnimal.class);
        intent.putExtra("animalChoice", animal);

        float[] velxarr = new float[velx.size()];
        for(int i = 0; i < velxarr.length - 1; i++) {
            velxarr[i] = velx.get(i);
        }
        intent.putExtra("velxarr", velxarr);

        float[] velyarr = new float[vely.size()];
        for(int i = 0; i < velyarr.length - 1; i++) {
            velyarr[i] = vely.get(i);
        }
        intent.putExtra("velyarr", velyarr);

        float[] maxvelxarr = new float[maxvelx.size()];
        for(int i = 0; i < maxvelxarr.length - 1; i++) {
            maxvelxarr[i] = maxvelx.get(i);
        }
        intent.putExtra("maxvelxarr", maxvelxarr);

        float[] maxvelyarr = new float[maxvely.size()];
        for(int i = 0; i < maxvelyarr.length - 1; i++) {
            maxvelyarr[i] = maxvely.get(i);
        }
        intent.putExtra("maxvelyarr", maxvelyarr);

        float[] accxarr = new float[accx.size()];
        for(int i = 0; i < accxarr.length - 1; i++) {
            accxarr[i] = accx.get(i);
        }
        intent.putExtra("accxarr", accxarr);

        float[] accyarr = new float[accy.size()];
        for(int i = 0; i < accyarr.length - 1; i++) {
            accyarr[i] = accy.get(i);
        }
        intent.putExtra("accyarr", accyarr);

        float[] acczarr = new float[accz.size()];
        for(int i = 0; i < acczarr.length - 1; i++) {
            acczarr[i] = accz.get(i);
        }
        intent.putExtra("acczarr", acczarr);

        startActivity(intent);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}