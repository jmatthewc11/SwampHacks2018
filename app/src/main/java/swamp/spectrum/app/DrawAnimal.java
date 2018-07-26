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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caitlin.autismdetector.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrawAnimal extends AppCompatActivity implements SensorEventListener {

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
        setContentView(R.layout.activity_draw_animal);

        Intent intent = getIntent();
        animalChoice = intent.getIntExtra("animalChoice", 0);

        float[] velxarr = intent.getFloatArrayExtra("velxarr");
        for(int i = 0; i < velxarr.length; i++) {
            velx.add(velxarr[i]);
        }

        float[] velyarr = intent.getFloatArrayExtra("velyarr");
        for(int i = 0; i < velyarr.length; i++) {
            vely.add(velyarr[i]);
        }

        float[] maxvelxarr = intent.getFloatArrayExtra("maxvelxarr");
        for(int i = 0; i < maxvelxarr.length; i++) {
            maxvelx.add(maxvelxarr[i]);
        }

        float[] maxvelyarr = intent.getFloatArrayExtra("maxvelyarr");
        for(int i = 0; i < maxvelyarr.length; i++) {
            maxvely.add(maxvelyarr[i]);
        }

        textAvtion = (TextView) findViewById(R.id.action2);
        textVelocityX = (TextView) findViewById(R.id.velocityx2);
        textVelocityY = (TextView) findViewById(R.id.velocityy2);
        textMaxVelocityX = (TextView) findViewById(R.id.maxvelocityx2);
        textMaxVelocityY = (TextView) findViewById(R.id.maxvelocityy2);
        textAccelerationX = (TextView) findViewById(R.id.accelerationx2);
        textAccelerationY = (TextView) findViewById(R.id.accelerationy2);
        textAccelerationZ = (TextView) findViewById(R.id.accelerationz2);

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
        ImageView view = (ImageView)findViewById(R.id.picture);

        switch(animalChoice){
            case 1:
                view.setImageResource(R.drawable.picture1);
                break;
            case 2:
                view.setImageResource(R.drawable.picture2);
                break;
            case 3:
                view.setImageResource(R.drawable.picture3);
                break;
            case 4:
                view.setImageResource(R.drawable.picture4);
                break;
            case 5:
                view.setImageResource(R.drawable.picture5);
                break;
            case 6:
                view.setImageResource(R.drawable.picture6);
                break;
            case 7:
                view.setImageResource(R.drawable.picture7);
                break;
            case 8:
                view.setImageResource(R.drawable.picture8);
                break;

        }

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
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                break;
        }
        return true;
    }

    public void startClassificaiton(View view) {

        double avgXVel = 0;
        for(int i = 0; i < velx.size(); i += 300) {
            avgXVel += velx.get(i);
        }
        avgXVel /= velx.size() + 1;

        double avgYVel = 0;
        for(int i = 0; i < vely.size(); i += 300) {
            avgYVel += vely.get(i);
        }
        avgYVel /= vely.size() + 1;

        double avgXAcc = 0;
        for(int i = 0; i < accx.size(); i += 300) {
            avgXAcc += accx.get(i);
        }
        avgXAcc /= accx.size() + 1;

        double avgYAcc = 0;
        for(int i = 0; i < accy.size(); i += 300) {
            avgYAcc += accy.get(i);
        }
        avgYAcc /= accy.size() + 1;

        double avgZAcc = 0;
        for(int i = 0; i < accz.size(); i += 300) {
            avgZAcc += accz.get(i);
        }
        avgZAcc /= accz.size() + 1;

        double maxXVel = 0;
        for(int i = 0; i < maxvelx.size(); i+= 300) {
            if(maxXVel < maxvelx.get(i)) {
                maxXVel = maxvelx.get(i);
            }
        }

        double maxYVel = 0;
        for(int i = 0; i < maxvely.size(); i+= 300) {
            if(maxYVel < maxvely.get(i)) {
                maxYVel = maxvely.get(i);
            }
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child("avg x-velocity").setValue(avgXVel);
        database.child("users").child("avg y-velocity").setValue(avgYVel);
        database.child("users").child("max x-velocity").setValue(maxXVel);
        database.child("users").child("max y-velocity").setValue(maxYVel);
        database.child("users").child("avg x-acc").setValue(avgXAcc);
        database.child("users").child("avg y-acc").setValue(avgYAcc);
        database.child("users").child("avg z-acc").setValue(avgZAcc);
        database.child("users").child("max x-acc").setValue(maxXAcc);
        database.child("users").child("max y-acc").setValue(maxYAcc);
        database.child("users").child("max z-acc").setValue(maxZAcc);

        Intent intent = new Intent(this, Classification.class);
        startActivity(intent);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
