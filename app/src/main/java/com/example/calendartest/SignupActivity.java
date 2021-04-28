package com.example.calendartest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText regfirstname, reglastname, regemail, regpass;
    private Button signup;
    ProgressBar progressbar;
    //FirebaseDatabase rootNode;
    //DatabaseReference reference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Input info
        regfirstname = findViewById(R.id.firstname);
        reglastname = findViewById(R.id.lastname);
        regemail = findViewById(R.id.email);
        regpass = findViewById(R.id.password);
        progressbar = findViewById(R.id.bar);

        //Firebase:
        FirebaseApp.initializeApp(this);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Check if login or not:
        if (fAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Sign up button
        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rootNode = FirebaseDatabase.getInstance();
                //reference = rootNode.getReference("User");

                String firstname = regfirstname.getText().toString();
                String lastname = reglastname.getText().toString();
                String email = regemail.getText().toString();
                String password = regpass.getText().toString();

                //Register set up
                if (TextUtils.isEmpty(email)){regemail.setError("Email is required");
                    return;}
                if (TextUtils.isEmpty(password)){ regpass.setError("Password is required");
                    return;}
                if (password.length() < 6){regpass.setError("Password must longer than 6");
                    return;}

                //Firebase register
                progressbar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Signed up successfully !", Toast.LENGTH_SHORT).show();

                            //Store data
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fname", firstname);
                            user.put("lname", lastname);
                            user.put("email", email);
                            user.put("password", password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for"+ userID);
                                }
                            });
                            ///

                            progressbar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //UserHelperClass userHelperClass = new UserHelperClass(firstname, lastname, phone, email, password);
                //reference.child(phone).setValue(userHelperClass);

                //Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                //startActivity(intent);
            }
        });

        //Change to log in page:
        TextView reglogin = findViewById(R.id.login);
        reglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    };
}