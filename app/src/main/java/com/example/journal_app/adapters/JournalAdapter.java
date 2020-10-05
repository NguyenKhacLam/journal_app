package com.example.journal_app.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journal_app.R;
import com.example.journal_app.modals.Journal;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalHolder> {
    private Context context;
    private ArrayList<Journal> journals;

    public JournalAdapter(Context context, ArrayList<Journal> journals) {
        this.context = context;
        this.journals = journals;
    }

    @NonNull
    @Override
    public JournalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new JournalHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalHolder holder, int position) {
        Journal journal = journals.get(position);
        String imgUrl;
        holder.title.setText(journal.getTitle());
        holder.thought.setText(journal.getThought());
        imgUrl = journal.getImageUrl();
        Glide.with(context).load(imgUrl).into(holder.img);
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(journal.getCreatedAt().getSeconds() * 1000);
        holder.date.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return journals.size() > 0 ? journals.size() : 0;
    }

    public class JournalHolder extends RecyclerView.ViewHolder {
        private TextView title, thought, date;
        private ImageView img;
        String userId, username;

        public JournalHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            title = itemView.findViewById(R.id.journal_title);
            thought = itemView.findViewById(R.id.journal_thought);
            date = itemView.findViewById(R.id.journal_time);
            img = itemView.findViewById(R.id.journal_img);
        }
    }
}
