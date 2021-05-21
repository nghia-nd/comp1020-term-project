package com.example.calendartest.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartest.EventManager;
import com.example.calendartest.R;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import io.perfmark.Link;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public CardManager mCardManager;

    private List<Card> meetingList = EventManager.cardList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mCardManager = new CardManager();
        mCardManager.init(getActivity(), getContext(), root, meetingList);
        return root;
    }
}