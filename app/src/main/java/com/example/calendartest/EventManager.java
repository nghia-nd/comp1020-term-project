package com.example.calendartest;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartest.ui.home.Card;
import com.example.calendartest.ui.home.CardAdapter;
import com.example.calendartest.ui.home.CardManager;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {
    public static ArrayList<CalendarEvent> eventList = new ArrayList<>();
    public static ArrayList<Card> cardList = new ArrayList<>();
    public static List<CalendarEvent> newList = new ArrayList<>();
    private static LinkedList<String> firebaseEventID = new LinkedList<>();
    public static Task<QuerySnapshot> querySnapshotTask;

    private static Calendar fillCalendar(int year, int month, int day, int hour, int min) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, min);
        return mCalendar;
    }

    public static void add(CalendarEvent event) {
        eventList.add(event);
        cardList.add(EventManager.convertToCard(event));
    }

    public static CalendarEvent convertToCalendarEvent(Context context, FormEvent formEvent) {
        String[] startTime = formEvent.getStartTime().split(":");
        String[] endTime = formEvent.getEndTime().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int endHour = Integer.parseInt(endTime[0]);
        int startMin = Integer.parseInt(startTime[1]);
        int endMin = Integer.parseInt(endTime[1]);
        String[] participants = formEvent.getParticipantEmail().split(" ");

        Calendar start = fillCalendar(formEvent.getYear(), formEvent.getMonth(), formEvent.getDay(), startHour,startMin);
        Calendar end = fillCalendar(formEvent.getYear(), formEvent.getMonth(), formEvent.getDay(), endHour, endMin);

        BaseCalendarEvent event = new BaseCalendarEvent(formEvent.getEventName(), "", formEvent.getEventLink(),
                ContextCompat.getColor(context, R.color.blue_selected), start, end, false, Arrays.asList(participants));
        event.setLocation(formEvent.getEventLink());
        return event;
    }

    public static Card convertToCard(CalendarEvent event) {
        String title = event.getTitle();
        Date date = event.getStartTime().getTime();
        List<String> participants = event.getParticipants();
        String address = event.getLocation();
        return new Card(title, date, participants, address);
    }

    public static List<CalendarEvent> fillEventList(Context context, Task<QuerySnapshot> task) {
        for(QueryDocumentSnapshot document : task.getResult()){
            if (!firebaseEventID.contains(document.getId())) {
                firebaseEventID.add(document.getId());

                String eventName = document.getString("eventName");
                String eventLink = document.getString("eventLink");
                String startTime = document.getString("startTime");
                String endTime = document.getString("endTime");
                String[] participants = document.getString("participantEmail").split(" ");
                int year = Math.toIntExact(document.getLong("year"));
                int month = Math.toIntExact(document.getLong("month"));
                int day = Math.toIntExact(document.getLong("day"));

                int startHour = Integer.parseInt(startTime.split(":")[0]);
                int startMin = Integer.parseInt(startTime.split(":")[1]);
                int endHour = Integer.parseInt(endTime.split(":")[0]);
                int endMin = Integer.parseInt(endTime.split(":")[01]);

                Calendar start = fillCalendar(year, month, day, startHour, startMin);
                Calendar end = fillCalendar(year, month, day, endHour, endMin);

                BaseCalendarEvent event = new BaseCalendarEvent(eventName, "", eventLink, ContextCompat.getColor(context, R.color.blue_selected), start, end, false, Arrays.asList(participants));
                Log.d("Event Manager: ", eventName);
                newList.add(event);
            }
        }
        return newList;
    }

    interface CallBack {
        void onExist(Context context, Task<QuerySnapshot> task);
    }

    public static List<CalendarEvent> pullFirebaseData(Context context, FirebaseAuth mAuth, CallBack callBack){         //, RecyclerView.Adapter adapter) {
        FirebaseUser currentuser = mAuth.getCurrentUser();
        String userID = currentuser.getUid();
        List<String> list_id = new ArrayList<String>();

        CollectionReference collectionRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userID).collection("events");

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    callBack.onExist(context, task);
                }
                else {
                    Log.d("Event Manager: ", "Cannot retrieve data");
                }
            }
        });

        return newList;
    }

    public static void pushToFirebase(Context context, FirebaseAuth mAuth, CalendarEvent event) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String eventName = event.getTitle();
        List<String> participants = new ArrayList<>(event.getParticipants());
        String participantString = "";
        for (String i : participants) {
            participantString += i + " ";
        }
        participantString = participantString.trim();
        String address = event.getLocation();
        Calendar startTime = event.getStartTime();
        Calendar endTime = event.getEndTime();

        CollectionReference eventsRef = FirebaseFirestore.getInstance()
                .collection("users").document(currentUser.getUid())
                .collection("events");

        // Adding the new event into the database
        Map<String, Object> user = new HashMap<>();
        user.put("eventName", eventName);
        user.put("participantEmail", participantString + " " + currentUser.getEmail());
        user.put("eventLink", address);
        user.put("year", startTime.get(Calendar.YEAR));
        user.put("month", startTime.get(Calendar.MONTH));
        user.put("day", startTime.get(Calendar.DAY_OF_MONTH));
        user.put("startTime", startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE));
        user.put("endTime", endTime.get(Calendar.HOUR_OF_DAY) + ":" + endTime.get(Calendar.MINUTE));
        user.put("currentUserID", currentUser.getUid());
        user.put("currentUserDisplay", currentUser.getEmail());
        eventsRef.add(user);

        for(String i : participants){

            String finalParticipantString = participantString;
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
                            user.put("eventName", eventName);
                            user.put("participantEmail", finalParticipantString + " " + currentUser.getEmail());
                            user.put("eventLink", address);
                            user.put("year", startTime.get(Calendar.YEAR));
                            user.put("month", startTime.get(Calendar.MONTH));
                            user.put("day", startTime.get(Calendar.DAY_OF_MONTH));
                            user.put("startTime", startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE));
                            user.put("endTime", endTime.get(Calendar.HOUR_OF_DAY) + ":" + endTime.get(Calendar.MINUTE));
                            participant.put("currentUserID", document.getId());
                            participant.put("currentUserDisplay", i);
                            eventsRef_participant.add(participant);

                            Log.d("userid", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("userid", "Error getting documents: ", task.getException());
                    }
                }});
        };
    }
}
