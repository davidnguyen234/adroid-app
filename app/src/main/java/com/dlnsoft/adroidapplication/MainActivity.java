package com.dlnsoft.adroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showCitiesToast(View view) {
        Toast t = Toast.makeText(this, "Cities", Toast.LENGTH_SHORT);
        t.show();
    }

    public void showFoodToast(View view) {
        Toast t = Toast.makeText(this, "Food", Toast.LENGTH_SHORT);
        t.show();
    }

    public void showMoviesToast(View view) {
        Toast t = Toast.makeText(this, "Movies", Toast.LENGTH_SHORT);
        t.show();
    }

    public void showMusicToast(View view) {
        Toast t = Toast.makeText(this, "Music", Toast.LENGTH_SHORT);
        t.show();
    }

    public void showParksToast(View view) {
        Toast t = Toast.makeText(this, "Parks", Toast.LENGTH_SHORT);
        t.show();
    }

    public void showTrafficToast(View view) {
        Toast t = Toast.makeText(this, "Traffic", Toast.LENGTH_SHORT);
        t.show();
    }
}