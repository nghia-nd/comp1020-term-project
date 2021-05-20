package com.example.calendartest;

import java.util.Calendar;
import java.util.Date;

public class FormEvent {
    private String eventName;
    private String participantEmail;
    private String eventLink;
    private String startTime;
    private String endTime;
    int year;
    int month;
    int day;

    private String userId;
    private String displayName;

    public FormEvent() {
        //empty constructor needed
    }

    public FormEvent(String eventName, String participantEmail, String eventLink, int year, int month, int day, String startTime, String endTime, String userId, String displayName) {
        this.eventName = eventName;
        this.participantEmail = participantEmail;
        this.eventLink = eventLink;
        this.year = year;
        this.month = month;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.displayName = displayName;
    }
    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String duration) {
        this.endTime = duration;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
