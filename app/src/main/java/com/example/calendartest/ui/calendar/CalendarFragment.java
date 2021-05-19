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

import com.example.calendartest.R;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.IDayItem;

import android.widget.Toast;

import java.util.ArrayList;
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

    private void fillList(List<CalendarEvent> eventList) {
        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.MONTH, 1);
        BaseCalendarEvent event1 = new BaseCalendarEvent("Thibault travels in Iceland", "A wonderful journey!", "Iceland",
                ContextCompat.getColor(this.getContext(), R.color.orange_dark), startTime1, endTime1, true);
        eventList.add(event1);

        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_YEAR, 3);
        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
                ContextCompat.getColor(this.getContext(), R.color.yellow), startTime2, endTime2, true);
        eventList.add(event2);

    }

    public class CalendarPicker implements CalendarPickerController {

        IDayItem mDayItem;
        CalendarEvent mEvent;
        Calendar mCalendar;
        @Override
        public void onDaySelected(IDayItem dayItem) {
            mDayItem = dayItem;
        }

        @Override
        public void onEventSelected(CalendarEvent event) {
            mEvent = event;
            Toast.makeText(getContext(), event.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onScrollToDate(Calendar calendar) {
            mCalendar = calendar;
        }
    }
}