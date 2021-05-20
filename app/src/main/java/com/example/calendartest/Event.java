package com.example.calendartest;

public class Event {
    private String eventName;
    private String participantEmail;
    private String eventLink;
    private String startTime;
    private String endTime;
    private int year;
    private int month;
    private int day;
    private int priority;

    private String userId;
    private String displayName;

    public Event() {
        //empty constructor needed
    }

    public Event(String eventName, String participantEmail, String eventLink, String startTime, String endTime, String userId, String displayName) {
        this.eventName = eventName;
        this.participantEmail = participantEmail;
        this.eventLink = eventLink;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.displayName = displayName;
        //this.priority = 1;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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
