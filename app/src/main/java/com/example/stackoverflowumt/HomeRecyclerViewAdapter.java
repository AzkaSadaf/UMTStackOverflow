package com.example.stackoverflowumt;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeRecyclerViewAdapter extends FirebaseRecyclerAdapter<UsersPosts, HomeRecyclerViewAdapter.ViewHolder> {

    public HomeRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<UsersPosts> options) {
        super(options);
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull UsersPosts usersPosts) {

        viewHolder.txtFullName.setText(usersPosts.Post_UserFullName);
        viewHolder.txtWriterUsername.setText(usersPosts.Post_Username);
        viewHolder.tvPostTitle.setText(usersPosts.Post_Title);
        viewHolder.tvPostDescription.setText(usersPosts.Post_Description);

        ArrayList<String> tags = new ArrayList<>(Arrays.asList(usersPosts.Post_Tags.split(",")));

        for (int index = 0; index < tags.size(); index++) {

            Chip chip = new Chip(viewHolder.itemView.getContext());
            chip.setText(tags.get(index));
            chip.setClickable(false);
            viewHolder.postTags.addView(chip);
        }

        Log.d("-----------Home--", "onBindViewHolder");

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;

        TextView txtFullName, txtWriterUsername, tvPostTitle, tvPostDescription;
        ChipGroup postTags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.postCardLayout);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtWriterUsername = itemView.findViewById(R.id.txtWriterUsername);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            postTags = itemView.findViewById(R.id.postTags);
        }
    }
}
