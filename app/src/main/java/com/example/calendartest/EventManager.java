package com.example.calendartest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartest.ui.home.Card;
import com.example.calendartest.ui.home.CardAdapter;
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
import java.util.Comparator;
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
    private static Task<QuerySnapshot> querySnapshotTask;

    private static Calendar fillCalendar(int year, int month, int day, int hour, int min) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, min);
        mCalendar.set(Calendar.SECOND, 0);
        return mCalendar;
    }

    public static class cardComparator implements Comparator<Card> {

        @Override
        public int compare(Card o1, Card o2) {
            int i = o1.getDate().getYear() - o2.getDate().getYear();
            if (i != 0) return i;

            i = o1.getDate().getMonth() - o2.getDate().getMonth();
            if (i != 0) return i;

            i = o1.getDate().getDate() - o2.getDate().getDate();
            if (i != 0) return i;

            i = o1.getDate().getHours() - o2.getDate().getHours();
            if (i != 0) return i;

            return o1.getDate().getMinutes() - o2.getDate().getMinutes();
        }
    }

    public static void add(CalendarEvent event) {
        Card cardEvent = EventManager.convertToCard(event);
        Card today = new Card("", Calendar.getInstance().getTime(), new LinkedList<>(), "");

        if(cardEvent.compareTo(today) >=0){
            cardList.add(cardEvent);
            cardList.sort(new cardComparator());
            eventList.add(event);
        }
        else{
            BaseCalendarEvent baseCalendarEvent = (BaseCalendarEvent) event;
            baseCalendarEvent.setColor(Color.parseColor("#FF5722"));
            eventList.add(event);
        }

    }

    public static void clear() {
        eventList.clear();
        cardList.clear();
        firebaseEventID.clear();
        newList.clear();
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

    public static void fillEventList(Context context) {
        if (querySnapshotTask.getResult() != null) {
            for (QueryDocumentSnapshot document : querySnapshotTask.getResult()) {
                if (!firebaseEventID.contains(document.getString("eventName"))) {
                    firebaseEventID.add(document.getString("eventName"));
                    String eventName = document.getString("eventName");
                    String eventLink = document.getString("eventLink");
                    String startTime = document.getString("startTime");
                    String endTime = document.getString("endTime");
                    String[] participants = document.getString("participantEmail").split(" ");
                    int year = Math.toIntExact(document.getLong("year"));
                    int month = Math.toIntExact(document.getLong("month")) + 1;
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
        }

        for (CalendarEvent i : newList) {
            EventManager.add(i);
        }
    }

    public static void pullFirebaseData(FirebaseAuth mAuth) {
        FirebaseUser currentuser = mAuth.getCurrentUser();
        String userID = currentuser.getUid();
        List<String> list_id = new ArrayList<String>();

        CollectionReference collectionRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userID).collection("events");

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("Event Manager: ", "Succesfully retrieved data");
                    querySnapshotTask = task;
                }
                else {
                    Log.d("Event Manager: ", "Cannot retrieve data");
                }
            }
        });
    }

    public static void pushToFirebase(FirebaseAuth mAuth, CalendarEvent event) {
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
