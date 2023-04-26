package com.example.securenotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private Button buttonpasswordreset;
    private EditText Emailforgotpassword;
    private FirebaseAuth authprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        //getSupportActionBar().setTitle("Forgot Password");
        Emailforgotpassword = findViewById(R.id.emailforgotpassword);
        buttonpasswordreset = findViewById(R.id.buttonforgotpassword);
        buttonpasswordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Emailforgotpassword.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Emailforgotpassword.setError("Email required ");
                    Emailforgotpassword.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Emailforgotpassword.setError(" Valid Email required ");
                    Emailforgotpassword.requestFocus();

                }
                else {
                    resetpassword(email);
                }
            }

            private void resetpassword(String email) {
                authprofile = FirebaseAuth.getInstance();
                authprofile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Check your inbox for a password reset link", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassword.this, Loginactivity.class);
                            //prevent the user from returning to Register activity on pressing the back button
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}