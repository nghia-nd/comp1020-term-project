package com.example.calendartest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    // Define the variable of CalendarView type
    // and TextView type;

    private RelativeLayout parent;
    private ConstraintLayout container;
    private FloatingActionButton fab;
    private Button change_to_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        parent = findViewById(R.id.parent);
        container = findViewById(R.id.container);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Add button clicked", Toast.LENGTH_SHORT).show();
                //showSnackbar();
                Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                startActivity(intent);
                //finish();

            }
        });

        EventManager.fillEventList(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


//        for (CalendarEvent i : EventManager.pullFirebaseData(this, FirebaseAuth.getInstance())) {
//            EventManager.add(i);
//        }
    }

    private void showSnackbar(){
        Snackbar.make(container, "this is a Snackbar", Snackbar.LENGTH_INDEFINITE)
                .setAction("Add Meeting", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Add Meeting clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}