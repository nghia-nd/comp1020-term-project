package com.example.calendartest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;

import com.example.calendartest.Event;
import com.example.calendartest.ui.calendar.CalendarFragment;
import com.example.calendartest.ui.home.HomeFragment;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventAdder {
    public static void addToFirebase(FirebaseUser currentUser, String eventName, String participantEmail_str, String eventLink, String startTime, String endTime) {
        String[] participantEmail = participantEmail_str.split(" ");
        CollectionReference eventsRef = FirebaseFirestore.getInstance()
                .collection("users").document(currentUser.getUid())
                .collection("events");

        // Adding the new event into the database
        Map<String, Object> user = new HashMap<>();
        user.put("eventName", eventName);
        user.put("participantEmail", participantEmail_str + " " + currentUser.getEmail());
        user.put("eventLink", eventLink);
        user.put("startTime", startTime);
        user.put("endTime", endTime);
        user.put("currentUserID", currentUser.getUid());
        user.put("currentUserDisplay", currentUser.getEmail());
        eventsRef.add(user);

        for(String i : participantEmail){

            FirebaseFirestore.getInstance()
                    .collection("users").whereEqualTo("email", i).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            //participantID.add(document.getId());

                            //DocumentReference participantRef = FirebaseFirestore.getInstance().collection("user").document(participantID);
                            CollectionReference eventsRef_participant = FirebaseFirestore.getInstance()
                                    .collection("users").document(document.getId())
                                    .collection("events");

                            // Adding the new event into the database
                            Map<String, Object> participant = new HashMap<>();
                            participant.put("eventName", eventName);
                            participant.put("participantEmail", participantEmail_str + " " + currentUser.getEmail());
                            participant.put("eventLink", eventLink);
                            participant.put("startTime", startTime);
                            participant.put("endTime", endTime);
                            participant.put("currentUserID", document.getId());
                            participant.put("currentUserDisplay", i);
                            eventsRef_participant.add(participant);

                            Event newEvent = new Event(eventName, participantEmail_str + " " + currentUser.getEmail(), eventLink, startTime, endTime,
                                    document.getId(), i);

                            Log.d("userid", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("userid", "Error getting documents: ", task.getException());
                    }
                }});
        };
    }

    public static void refreshAllFragment(Activity activity) {
        Fragment calendarFragment = activity.getFragmentManager().findFragmentByTag("CalendarFragment");
        Fragment homeFragement = activity.getFragmentManager().findFragmentByTag("HomeFragment");
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.detach(calendarFragment);
        fragmentTransaction.detach(homeFragement);
        fragmentTransaction.attach(calendarFragment);
        fragmentTransaction.attach(homeFragement);
        fragmentTransaction.commit();
    }

}
