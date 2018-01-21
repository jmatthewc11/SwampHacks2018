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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caitlin.autismdetector.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrawAnimal extends AppCompatActivity {

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

    SensorManager mSensorManager;
    Sensor mSensor;

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

        float[] accxarr = intent.getFloatArrayExtra("accxarr");
        for(int i = 0; i < accxarr.length; i++) {
            accx.add(accxarr[i]);
        }

        float[] accyarr = intent.getFloatArrayExtra("accyarr");
        for(int i = 0; i < accyarr.length; i++) {
            accy.add(accyarr[i]);
        }

        float[] acczarr = intent.getFloatArrayExtra("acczarr");
        for(int i = 0; i < acczarr.length; i++) {
            accz.add(acczarr[i]);
        }

        textAvtion = (TextView) findViewById(R.id.action2);
        textVelocityX = (TextView) findViewById(R.id.velocityx2);
        textVelocityY = (TextView) findViewById(R.id.velocityy2);
        textMaxVelocityX = (TextView) findViewById(R.id.maxvelocityx2);
        textMaxVelocityY = (TextView) findViewById(R.id.maxvelocityy2);
        textAccelerationZ = (TextView) findViewById(R.id.accelerationz2);
        textAccelerationX = (TextView) findViewById(R.id.accelerationx2);
        textAccelerationY = (TextView) findViewById(R.id.accelerationy2);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

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
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                break;
        }
        return true;
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

        double avgXAcc = 0.00001;
        for(int i = 0; i < accx.size(); i+=300) {
            avgXAcc += (accx.get(i));
        }
        avgXAcc /= accx.size() + 1;

        double avgYAcc = 0.00001;
        for(int i = 0; i < accy.size(); i+=300) {
            avgYAcc += (accy.get(i) + 1);
        }

        double avgZAcc = 0.00001;
        for(int i = 0; i < accz.size(); i+=300) {
            avgZAcc += accz.get(i);
        }
        avgZAcc /= (accz.size() + 1);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child("avg x-velocity").setValue(avgXVel);
        database.child("users").child("avg y-velocity").setValue(avgYVel);
        database.child("users").child("max x-velocity").setValue(maxXVel);
        database.child("users").child("max y-velocity").setValue(maxYVel);

        try {
            database.child("users").child("avg x-acceleration").setValue(avgXAcc);
        } catch(Exception e) {
            database.child("users").child("avg x-acceleration").setValue(0);
        }

        try {
            database.child("users").child("avg y-acceleration").setValue(avgYAcc);
        } catch (Exception e) {
            database.child("users").child("avg y-acceleration").setValue(0);
        }

        try {
            database.child("users").child("avg z-acceleration").setValue(avgZAcc);
        } catch (Exception e) {
            database.child("users").child("avg z-acceleration").setValue(0);
        }

        Intent intent = new Intent(this, Classification.class);
        startActivity(intent);
    }
}
