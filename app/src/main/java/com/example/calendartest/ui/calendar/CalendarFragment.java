package com.example.calendartest.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendartest.EventManager;
import com.example.calendartest.MainActivity;
import com.example.calendartest.NewEventActivity;
import com.example.calendartest.R;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.IDayItem;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CalendarFragment extends Fragment {
    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calender;
    TextView date_view;

    private CalendarViewModel calendarViewModel;
    private AgendaCalendarView mAgendaCalendarView;
    private CalendarPickerController mCalendarPickerController;
    private CalendarPicker mCalendarPicker = new CalendarPicker();
    private List<CalendarEvent> eventList = EventManager.eventList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        ProgressBar progressBar = root.findViewById(R.id.progress_bar1);

        mAgendaCalendarView = (AgendaCalendarView) root.findViewById(R.id.calender);
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), new CalendarPicker());

        progressBar.setVisibility(View.INVISIBLE);

        return root;
    }

    public class CalendarPicker implements CalendarPickerController {

        IDayItem mDayItem;
        CalendarEvent mCalendarEvent;
        Calendar mCalendar;
        @Override
        public void onDaySelected(IDayItem dayItem) {
            mDayItem = dayItem;
        }

        @Override
        public void onEventSelected(CalendarEvent event) {
            if (event != null) {
                mCalendarEvent = event;
                String title = event.getTitle();
                String location = event.getLocation();
                List<String> participants = event.getParticipants();
                int month = event.getDayReference().getDate().getMonth();
                int day = event.getDayReference().getDate().getDate();
                int hour = event.getStartTime().get(Calendar.HOUR);
                int min = event.getStartTime().get(Calendar.MINUTE);
                Toast.makeText(getContext(), event.getTitle() + " clicked " + day + "/" + month + " | " + hour + ":" + min, Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getContext(), NewEventActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onScrollToDate(Calendar calendar) {
            mCalendar = calendar;
        }
    }
}