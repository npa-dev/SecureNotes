package com.example.securenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class biometricauthentication extends AppCompatActivity {
    Button authbtn;

    TextView tvAuthStatus;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometricauthentication);
        authbtn = findViewById(R.id.authbtn);


        //initials values
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(biometricauthentication.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //error during authentication
                tvAuthStatus.setText("Error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Authentication successful
                //tvAuthStatus.setText("Authentication Successful");
                Intent intent = new Intent(biometricauthentication.this, Home.class);
                //prevent the user from returning to Register activity on pressing the back button
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //Authentication failed
                //tvAuthStatus.setText("Authentication Failed");
            }
        });

        // set up title, description, on authentication dialog
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Cancel")
                .build();


        authbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show authentication dialog
                biometricPrompt.authenticate(promptInfo);

            }
        });


    }
}