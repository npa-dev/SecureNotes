package com.example.securenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class  Loginactivity extends AppCompatActivity {
    public EditText inputLoginEmail, inputLoginPassword;
    public ProgressBar progressBar;
    public FirebaseAuth authprofile;
    static final String TAG = "Loginactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        inputLoginEmail = findViewById(R.id.LoginEmail);
        inputLoginPassword = findViewById(R.id.LoginPassword);

        authprofile = FirebaseAuth.getInstance();

        TextView button = findViewById(R.id.ForgotPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Loginactivity.this, ForgotPassword.class));
            }
        });

        //Login button
        TextView button1 = findViewById(R.id.login);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginemail = inputLoginEmail.getText().toString();
                String loginpassword = inputLoginPassword.getText().toString();

                if (TextUtils.isEmpty(loginemail)) {
                    Toast.makeText(Loginactivity.this, "Enter your email", Toast.LENGTH_LONG).show();
                    inputLoginEmail.setError("Valid email required");
                    inputLoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
                    Toast.makeText(Loginactivity.this, "Re-enter your email", Toast.LENGTH_LONG).show();
                    inputLoginEmail.setError("Valid email required");
                    inputLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(loginpassword)) {
                    Toast.makeText(Loginactivity.this, "Enter your password", Toast.LENGTH_LONG).show();
                    inputLoginPassword.setError("Password is required");
                    inputLoginPassword.requestFocus();
                } else {
                    loginuser(loginemail, loginpassword);
                }

            }


            private void loginuser(String loginemail, String loginpassword) {
                authprofile.signInWithEmailAndPassword(loginemail, loginpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //startActivity(new Intent(Loginactivity.this, Home.class));
                            Intent intent = new Intent(Loginactivity.this, biometricauthentication.class);
                            startActivity(intent);
                            Toast.makeText(Loginactivity.this, "You are now logged in", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidUserException e) {
                                inputLoginEmail.setError("User does not exits or is no longer valid. Please register again");
                                inputLoginEmail.requestFocus();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                inputLoginEmail.setError("Invalid credentials. Kindly check and re-enter your details.");
                                inputLoginEmail.requestFocus();
                            }
                            catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(Loginactivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
        TextView btn = findViewById(R.id.signup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginactivity.this, Registration.class));
            }
        });
    }
}