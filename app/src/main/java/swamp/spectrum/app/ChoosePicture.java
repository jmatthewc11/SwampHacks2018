package swamp.spectrum.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.caitlin.autismdetector.R;

public class ChoosePicture extends AppCompatActivity {

    private int animalChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_picture);
    }

    public void image1(View view) {
        drawCircle(1);
    }

    public void image2(View view) {
        drawCircle(2);
    }

    public void image3(View view) {
        drawCircle(3);
    }

    public void image4(View view) {
        drawCircle(4);
    }

    public void image5(View view) {
        drawCircle(5);
    }

    public void image6(View view) {
        drawCircle(6);
    }

    public void image7(View view) {
        drawCircle(7);
    }

    public void image8(View view) {
        drawCircle(8);
    }

    public void drawCircle(int animal) {
        Intent intent = new Intent(this, DrawCircle.class);
        intent.putExtra("animalChoice", animal);
        startActivity(intent);
    }

}
