package com.example.calendartest.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartest.R;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private Context mContext;
    private List<Card> mCardList;

    public CardAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Card> list) {
        mCardList = list;
        notifyDataSetChanged();
    }

    public void clearData() {
        mCardList = new LinkedList<>();
    }

    public String toValidUrl(String url) {
        String header = "https";
        if (!url.contains("http") && !url.contains("https")) {
            return "https://" + url;
        }
        return url;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    public void openLink(String url) {
        String Url = toValidUrl(url);

        String http = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern pattern = Pattern.compile(http);
        Matcher matcher = pattern.matcher(Url);

        if(matcher.find()){
            Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
            mContext.startActivity(web);
        }
        else{
            Toast.makeText(mContext, "Cannot open the link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = mCardList.get(position);
        if (card == null) {
            return;
        }
        holder.viewTitle.setText(card.getTitle().toUpperCase());
        holder.viewDate.setText("Time: " + card.getDate());
        holder.viewUrl.setText("Address: " + card.getUrlOrLocation());
        LinkedList<String> participants = new LinkedList<String>(card.getParticipants());
        String strParticipants = "";
        int i;
        for (i = 0; i < participants.size() - 1; i++) {
            strParticipants += participants.poll() + ", ";
        }
        strParticipants += participants.poll();

        holder.viewParticipants.setText("Participant(s): " + strParticipants);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                openLink(card.getUrlOrLocation());
            }
        });
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(card.getUrlOrLocation());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCardList != null) {
            return mCardList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView viewTitle;
        private TextView viewDate;
        private TextView viewUrl;
        private TextView viewParticipants;
        private com.google.android.material.button.MaterialButton viewButton;

        private ItemClickListener itemClickListener;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTitle = itemView.findViewById(R.id.text_title);
            viewDate = itemView.findViewById(R.id.text_date);
            viewParticipants = itemView.findViewById(R.id.text_participant);
            viewUrl = itemView.findViewById(R.id.text_url);
            viewButton = itemView.findViewById(R.id.text_button);
            itemView.setOnClickListener(this);
            viewButton.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
