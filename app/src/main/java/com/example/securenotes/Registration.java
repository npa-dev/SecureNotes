package com.example.securenotes;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    TextView alreadyHaveaccount;
    EditText inputEmail, inputPassword, inputConfirmPassword, inputFullName;
    Button btnRegister;
    static final String TAG = "Registration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toast.makeText(Registration.this, "you can register now", Toast.LENGTH_LONG).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveaccount = findViewById(R.id.alreadyHaveaccount2);
        inputFullName = findViewById(R.id.inputFullName2);
        inputEmail = findViewById(R.id.inputEmail2);
        inputPassword = findViewById(R.id.inputPassword2);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword2);


        btnRegister = findViewById(R.id.btnRegister2);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String fullname = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmpassword = inputConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(Registration.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    inputFullName.setError("Full name is required");
                    inputFullName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Registration.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    inputEmail.setError("Email is required");
                    inputEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Registration.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    inputPassword.setError("Password is required");
                } else if (password.length() < 6) {
                    Toast.makeText(Registration.this, "password should be at least 6 characters", Toast.LENGTH_LONG).show();
                    inputPassword.setError("Password too Weak");
                    inputPassword.requestFocus();
                } else if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(Registration.this, "Please enter your password again", Toast.LENGTH_LONG).show();
                    inputConfirmPassword.setError("Confirm your password");
                    inputConfirmPassword.requestFocus();

                    inputPassword.requestFocus();
                } else if (!password.equals(confirmpassword)) {
                    Toast.makeText(Registration.this, "Please same password", Toast.LENGTH_LONG).show();
                    inputPassword.setError("Password confirmation required");
                    inputPassword.requestFocus();
                    //Clear the entered passwords
                    inputPassword.clearComposingText();
                    inputConfirmPassword.clearComposingText();
                } else {
                    PerforAuth(fullname, email, password);
                }
            }
        });

        TextView btn = findViewById(R.id.alreadyHaveaccount2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Loginactivity.class));
            }
        });
    }

    private void PerforAuth(String fullname, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    // store user details in the database
                    ReadwriteUserDetails writeDetails = new ReadwriteUserDetails(fullname, email);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference referenceProfile = database.getReference("Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // send verification email
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(Registration.this, "user registered successfully. please check your email to verify.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registration.this, Loginactivity.class);
                                //prevent the user from returning to Register activity on pressing the back button
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Registration.this,"Registration failed...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    try{
                        throw task.getException();

                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        inputEmail.setError(("your email is invalid. kindly re-enter your email."));
                        inputEmail.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        inputEmail.setError(("email already in use. kindly cross check email."));
                        inputEmail.requestFocus();
                    }
                    catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
