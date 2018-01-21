package swamp.spectrum.app;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.TextView;

import com.example.caitlin.autismdetector.R;

import java.util.ArrayList;

public class DrawCircle extends AppCompatActivity {

    ArrayList<Float> velx = new ArrayList<Float>();
    ArrayList<Float> vely = new ArrayList<Float>();
    ArrayList<Float> maxvelx = new ArrayList<Float>();
    ArrayList<Float> maxvely = new ArrayList<Float>();
    ArrayList<Float> accx = new ArrayList<Float>();
    ArrayList<Float> accy = new ArrayList<Float>();
    ArrayList<Float> accz = new ArrayList<Float>();

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

    SensorManager mSensorManager;
    Sensor mSensor;

    public int animalChoice = 0;
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
        textAccelerationZ = (TextView) findViewById(R.id.accelerationz);
        textAccelerationX = (TextView) findViewById(R.id.accelerationx);
        textAccelerationY = (TextView) findViewById(R.id.accelerationy);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }

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

                textVelocityX.setText("X-velocity (pixel/s): 0");
                textVelocityY.setText("Y-velocity (pixel/s): 0");
                textMaxVelocityX.setText("max. X-velocity: 0");
                textMaxVelocityY.setText("max. Y-velocity: 0");
                textAccelerationX.setText("X-acceleration: 0");
                textAccelerationY.setText("Y-acceleration: 0");
                textAccelerationZ.setText("Z-acceleration: 0");

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

    public void onSensorChanged(SensorEvent se)
    {

        float accX = se.values[SensorManager.DATA_X];
        accx.add(accX);
        float accY = se.values[SensorManager.DATA_Y];
        accy.add(accY);
        float accZ = se.values[SensorManager.DATA_Z];
        accz.add(accZ);
        long now = System.currentTimeMillis();

        textAccelerationX.setText("X-acceleration: " + accX);
        textAccelerationY.setText("Y-acceleration: " + accY);
        textAccelerationZ.setText("Z-acceleration: " + accZ);
    }
}


