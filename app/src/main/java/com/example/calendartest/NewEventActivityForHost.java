package com.example.calendartest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

public class NewEventActivityForHost extends AppCompatActivity {
    private EditText new_event_eventName;
    private EditText new_event_participantEmail;
    private EditText new_event_eventLink;
    private Button select_startTime;
    private Button select_endTime;
    private TextView new_event_startTime;
    private TextView new_event_endTime;
    LinearTimePickerDialog dialogStartTime;
    LinearTimePickerDialog dialogEndTime;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String startTimeInt;
    String endTimeInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        new_event_eventName = findViewById(R.id.new_event_eventName_editText);
        new_event_participantEmail = findViewById(R.id.new_event_Participant_email_editText);
        new_event_eventLink = findViewById(R.id.new_event_eventLink_editText);

        new_event_startTime = findViewById(R.id.new_event_startTime_textView);
        new_event_endTime = findViewById(R.id.new_event_endTime_textView);
        select_startTime = findViewById(R.id.new_event_startTime_button);
        select_endTime = findViewById(R.id.new_event_endTime_button);


        dialogStartTime = LinearTimePickerDialog.Builder.with(this)
                .setTextColor(Color.parseColor("#ffffff"))
                .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                    @Override
                    public void onPositive(DialogInterface dialog, int hour, int minutes) {
                        String setHour = hour + "";
                        String setMinutes = minutes + "";


                        if (minutes == 0) {
                            setMinutes = "00";
                        }
                        if (hour < 10) {
                            setHour = "0" + hour;
                        }

                        new_event_startTime.setText(setHour + ":" + setMinutes);
                        startTimeInt = "" + setHour + setMinutes;
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {

                    }
                })
                .build();
        select_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogStartTime.show();
            }
        });

        dialogEndTime = LinearTimePickerDialog.Builder.with(this)
                .setTextColor(Color.parseColor("#ffffff"))
                .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                    @Override
                    public void onPositive(DialogInterface dialog, int hour, int minutes) {
                        String setHour = hour + "";
                        String setMinutes = minutes + "";


                        if (minutes == 0) {
                            setMinutes = "00";
                        }
                        if (hour < 10) {
                            setHour = "0" + hour;
                        }

                        new_event_endTime.setText(setHour + ":" + setMinutes);
                        endTimeInt = "" + setHour + setMinutes;
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {

                    }
                })
                .build();
        select_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEndTime.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_event_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_event: // When save icon is clicked
                saveEvent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Saves the new event into the database
    private void saveEvent() {
        String eventName = new_event_eventName.getText().toString();
        String participantEmail_str = new_event_participantEmail.getText().toString();
        String[] participantEmail = new_event_participantEmail.getText().toString().split(" ");
        String eventLink = new_event_eventLink.getText().toString();
        String startTime = new_event_startTime.getText().toString();
        String endTime = new_event_endTime.getText().toString();

        // Ensures event fields are non-empty
        if (eventName.trim().isEmpty()) {
            new_event_eventName.setError("Event name required");
            new_event_eventName.requestFocus();
            return;
        }
        if (startTime.trim().isEmpty()) {
            new_event_startTime.setError("Start time required");
            Toast.makeText(this, "Start time required", Toast.LENGTH_SHORT).show();
            new_event_startTime.requestFocus();
            return;
        }
        if (endTime.trim().isEmpty()) {
            new_event_endTime.setError("End time required");
            Toast.makeText(this, "End time required", Toast.LENGTH_SHORT).show();
            new_event_endTime.requestFocus();
            return;
        }

        if (Integer.parseInt(startTimeInt) >= Integer.parseInt(endTimeInt)) {
            Toast.makeText(this, "End time cannot be earlier than start time!", Toast.LENGTH_SHORT).show();
            new_event_endTime.requestFocus();
            return;
        }

        // Retrieving the date selected on the calendar from an intent
        Intent intent = getIntent();

        // Add event to Firebase
        //EventAdder eventAdder = new EventAdder();
        EventAdder.addToFirebase(currentUser, eventName, participantEmail_str, eventLink, startTime, endTime);
        EventAdder.refreshAllFragment(getParent());
        /**New event object**/
        //Event newEvent = new Event(eventName, participantEmail, eventLink, startTime, endTime,
                //currentUser.getUid(), currentUser.getDisplayName());

        Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
