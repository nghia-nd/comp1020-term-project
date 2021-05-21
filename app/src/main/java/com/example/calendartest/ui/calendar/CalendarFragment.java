package com.example.calendartest.ui.calendar;

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


    private CalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);




//        for (CalendarEvent i : EventManager.pullFirebaseData(getActivity(), FirebaseAuth.getInstance(), mAgendaCalendarView.getWeeksAdapter())) {
//            EventManager.add(i);
//        }

        return root;
    }

}