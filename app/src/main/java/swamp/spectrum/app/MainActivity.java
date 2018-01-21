package swamp.spectrum.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.caitlin.autismdetector.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Continue button */
    public void continueButton(View view) {
        Intent intent = new Intent(this, ChoosePicture.class);
        startActivity(intent);
    }
}
