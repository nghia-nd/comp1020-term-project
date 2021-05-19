package com.example.calendartest.ui.home;

public class Card {
    private String title;
    private String startTime;
    private String day;
    private String participants;
    private String urlOrLocation;

    public Card(String title, String startTime, String day, String participants, String urlOrLocation) {
        this.title = title;
        this.startTime = startTime;
        this.day = day;
        this.participants = participants;
        this.urlOrLocation = urlOrLocation;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUrlOrLocation() {
        return urlOrLocation;
    }

    public void setUrlOrLocation(String urlOrLocation) {
        this.urlOrLocation = urlOrLocation;
    }
}
