package com.example.calendartest.ui.calendar;

import android.content.Context;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.IDayItem;

import java.util.Calendar;
import java.util.List;

public class CalendarPicker implements CalendarPickerController {

    IDayItem mDayItem;
    CalendarEvent mCalendarEvent;
    Calendar mCalendar;
    Context mContext;

    public CalendarPicker(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDaySelected(IDayItem dayItem) {
        mDayItem = dayItem;
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        mCalendarEvent = event;
        String title = event.getTitle();
        String location = event.getLocation();
        List<String> participants = event.getParticipants();
        int month = event.getDayReference().getDate().getMonth();
        int day = event.getDayReference().getDate().getDate();
        int hour = event.getStartTime().get(Calendar.HOUR);
        int min = event.getStartTime().get(Calendar.MINUTE);
        Toast.makeText(mContext, title + " clicked " + day + "/" + month + " | " + hour + ":" + min, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        mCalendar = calendar;
    }
}
