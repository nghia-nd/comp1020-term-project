package com.example.calendartest.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.calendartest.MainActivity;
import com.example.calendartest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText logemail, logpass;
    ProgressBar progressbar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logemail = findViewById(R.id.logmail);
        logpass = findViewById(R.id.logpassword);
        login = findViewById(R.id.login);
        progressbar = findViewById(R.id.bar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = logemail.getText().toString();
                String password = logpass.getText().toString();

                //Register set up
                if (TextUtils.isEmpty(email)){logemail.setError("Email is required");
                    return;}
                if (TextUtils.isEmpty(password)){ logpass.setError("Password is required");
                    return;}
                if (password.length() < 6){logpass.setError("Password must longer than 6");
                    return;}

                //Firebase register
                progressbar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in successfully !", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}