package com.example.calendartest.home;

import java.util.Date;
import java.util.List;

public class Card implements Comparable<Card>{
    private String title;
    private Date date;
    private List<String> participants;
    private String urlOrLocation;

    public Card(String title, Date date, List<String> participants, String urlOrLocation) {
        this.title = title;
        this.date =date;
        this.participants = participants;
        this.urlOrLocation = urlOrLocation;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrlOrLocation() {
        return urlOrLocation;
    }

    public void setUrlOrLocation(String urlOrLocation) {
        this.urlOrLocation = urlOrLocation;
    }

    @Override
    public int compareTo(Card o) {
        int i = date.getYear() - o.date.getYear();
        if (i != 0) return i;

        i = date.getMonth() - o.date.getMonth();
        if (i != 0) return i;

        i = date.getDate() - o.date.getDate();
        if (i != 0) return i;

        i = date.getHours() - o.date.getHours();
        if (i != 0) return i;

        return date.getMinutes() - o.date.getMinutes();
    }
}
