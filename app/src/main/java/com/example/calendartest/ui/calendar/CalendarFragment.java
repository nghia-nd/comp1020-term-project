package com.example.calendartest.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendartest.Event;
import com.example.calendartest.R;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.IDayItem;
import com.github.tibolte.agendacalendarview.models.IWeekItem;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import io.perfmark.Link;


public class CalendarFragment extends Fragment {
    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calender;
    TextView date_view;

    private CalendarViewModel calendarViewModel;
    private AgendaCalendarView mAgendaCalendarView;
    private CalendarPickerController mCalendarPickerController;
    private CalendarPicker mCalendarPicker = new CalendarPicker();

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

        List<CalendarEvent> eventList = new ArrayList<>();
        fillList(eventList);

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), new CalendarPicker());

        progressBar.setVisibility(View.INVISIBLE);

        return root;
    }

    private Calendar fillCalendar(int year, int month, int day, int hour, int min) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, min);

        return mCalendar;
    }

    private void fillList(List<CalendarEvent> eventList) {
        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_YEAR, 0);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_YEAR, 4);
        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
                ContextCompat.getColor(this.getContext(), R.color.orange_dark), startTime2, endTime2, false, new LinkedList<String>());
        eventList.add(event2);

        Calendar startTime1 = fillCalendar(2021, 5, 20, 15,30);
        Calendar endTime1 = fillCalendar(2021, 5, 20, 15, 50);
        BaseCalendarEvent event1 = new BaseCalendarEvent("Deadlines", "A beautiful deadline", "Dalvík",
                ContextCompat.getColor(this.getContext(), R.color.blue_selected), startTime1, endTime1, true, new LinkedList<String>());
        eventList.add(event1);

    }

    public class CalendarPicker implements CalendarPickerController {

        IDayItem mDayItem;
        CalendarEvent mCalendarEvent;
        Calendar mCalendar;
        Event mEvent;
        @Override
        public void onDaySelected(IDayItem dayItem) {
            mDayItem = dayItem;
        }

        @Override
        public void onEventSelected(CalendarEvent event) {
            mCalendarEvent = event;
            String title = event.getTitle();
            String location = event.getLocation();
            LinkedList<String> participants = event.getParticipants();
            Date date = event.getDayReference().getDate();
            int month = date.getMonth();
            int day = date.getDate();
            Toast.makeText(getContext(), event.getTitle() + " clicked " + day + "/" + month, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onScrollToDate(Calendar calendar) {
            mCalendar = calendar;
        }
    }
}