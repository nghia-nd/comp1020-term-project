package com.example.calendartest.ui.home;

public class Card {
    private String title;
    private String startTime;
    private String urlOrLocation;

    public Card(String title, String startTime, String urlOrLocation) {
        this.title = title;
        this.startTime = startTime;
        this.urlOrLocation = urlOrLocation;
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
