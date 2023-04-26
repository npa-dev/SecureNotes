package com.example.securenotes;

import static com.example.securenotes.R.*;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_landingpage);
        Button goToLoginPageButton = findViewById(R.id.gettingstarted);
        if(goToLoginPageButton != null) {
            goToLoginPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Loginactivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
