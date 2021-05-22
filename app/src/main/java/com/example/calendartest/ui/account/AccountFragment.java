package com.example.calendartest.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendartest.R;
import com.example.calendartest.ui.auth.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {
    Button logout;
    private AccountViewModel accountViewModel;
    private TextView fname, lname, email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        View root = inflater.inflate(R.layout.fragment_account, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Logout button
        logout = root.findViewById(R.id.buttonlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), SignupActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //Show user info
        fname = root.findViewById(R.id.firstname);
        lname = root.findViewById(R.id.lastname);
        email = root.findViewById(R.id.email);
        ProgressBar progressBar = root.findViewById(R.id.progress_bar);

        String user_id = mAuth.getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Log.d("Document", task.getResult().getData().toString());
                        String fname_fb = task.getResult().getString("fname");
                        String lname_fb = task.getResult().getString("lname");
                        String email_fb = task.getResult().getString("email");
                        fname.setText("First name: " + fname_fb);
                        lname.setText("Last name: " + lname_fb);
                        email.setText("Email: " + email_fb);
                    }
                }

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return root;
    }
}