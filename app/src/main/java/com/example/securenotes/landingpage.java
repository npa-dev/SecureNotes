package com.example.securenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class landingpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);

        Button btn = findViewById(R.id.gettingstarted); // reference to the button

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(landingpage.this, Loginactivity.class)); // capitalized Intent and Loginactivity
            }
        });
    }
}
