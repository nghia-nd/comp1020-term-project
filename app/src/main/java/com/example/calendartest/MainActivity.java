package com.example.calendartest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.calendartest.ui.calendar.CalendarFragment;
import com.example.calendartest.ui.calendar.CalendarPicker;
import com.example.calendartest.ui.home.Card;
import com.example.calendartest.ui.home.CardManager;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // Define the variable of CalendarView type
    // and TextView type;

    private RelativeLayout parent;
    private ConstraintLayout container;
    private FloatingActionButton fab;
    private Button change_to_account;

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calender;
    TextView date_view;
    private AgendaCalendarView mAgendaCalendarView;
    private CalendarPickerController mCalendarPickerController;
    private CalendarPicker mCalendarPicker = new CalendarPicker(this);
    private List<CalendarEvent> eventList = EventManager.eventList;


    private CardManager cardManager;

    private List<Card> meetingList = EventManager.cardList;



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

            }
        });



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        EventManager.pullFirebaseData(this, FirebaseAuth.getInstance(), new EventManager.CallBack() {
            @Override
            public void onExist(Context context, Task<QuerySnapshot> task) {
                EventManager.fillEventList(context, task);
                EventManager.querySnapshotTask = task;
                setUpRecycler(EventManager.fillEventList(getBaseContext(), task));
            }
        });
//
//        EventManager.fillEventList(this, EventManager.querySnapshotTask);
//
//        for (CalendarEvent i : EventManager.newList) {
//            EventManager.add(i);
//        }

    }

    private void setUpRecycler(List<CalendarEvent> eventList) {
        mAgendaCalendarView = this.findViewById(R.id.calender);
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), mCalendarPicker);
        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        ProgressBar progressBar = findViewById(R.id.progress_bar1);
        progressBar.setVisibility(View.INVISIBLE);

        cardManager = new CardManager();
        cardManager.init(this, this, meetingList);
//        for (CalendarEvent i : EventManager.pullFirebaseData(getActivity(), FirebaseAuth.getInstance(), cardManager.getCardAdapter())) {
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