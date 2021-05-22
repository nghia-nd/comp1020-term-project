package com.example.calendartest.home;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartest.event.EventManager;
import com.example.calendartest.R;

import java.util.List;

public class CardManager {
    private RecyclerView cardRecyclerView;
    private static CardAdapter mCardApdater;

    private List<Card> meetingList = EventManager.cardList;

    public void init(Activity activity, Context context, View view, List<Card> meetingList)  {
        cardRecyclerView = view.findViewById(R.id.card_recyclerView);
        LinearLayoutManager mLinearLayoutManager= new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        mCardApdater = new CardAdapter(activity);
        cardRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCardApdater.setData(meetingList);
        cardRecyclerView.setAdapter(mCardApdater);
    }

    public CardAdapter getCardAdapter() {
        return mCardApdater;
    }
}