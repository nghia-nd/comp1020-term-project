package com.example.calendartest.ui.calendar;

import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.IDayItem;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.Calendar;

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
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        mCalendar = calendar;
    }
}
