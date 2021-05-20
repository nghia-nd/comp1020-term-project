package com.example.calendartest.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartest.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import io.perfmark.Link;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView cardRecyclerView;
    private CardAdapter mCardApdater;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        cardRecyclerView = root.findViewById(R.id.card_recyclerView);
        LinearLayoutManager mLinearLayoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mCardApdater = new CardAdapter(getActivity());
        cardRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCardApdater.setData(getMeeting());
        cardRecyclerView.setAdapter(mCardApdater);
        return root;
    }

    private List<Card> getMeeting() {
        List<Card> meetingList = new ArrayList<Card>();
        LinkedList<String> participants = new LinkedList<>();
        participants.add("Nghia");
        participants.add("Khoi");
        meetingList.add(new Card("first meeting", Calendar.getInstance().getTime(), participants, "https://google.com"));
        meetingList.add(new Card("second meeting", Calendar.getInstance().getTime(), participants,"vinuni-edu-vn.zoom.us/j/93195709386?pwd=R1pRK1l2UE1zRjRwL1NBTnJYdHZUQT09"));

        return meetingList;
    }
}