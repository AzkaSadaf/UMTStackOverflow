package com.example.stackoverflowumt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    View view;
    FloatingActionButton btnAddNewPost;
    RecyclerView rvHomePosts;
    FirebaseRecyclerAdapter adapter;
    HashMap<String, String> postsMap;
    ArrayList<UsersPosts> finale;
    DatabaseReference firebaseUserDB;
    DatabaseReference firebasePostDB;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        rvHomePosts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        Log.d("-----------Home--", "onActivityCreated");

        FirebaseRecyclerOptions<UsersPosts> options =
                new FirebaseRecyclerOptions.Builder<UsersPosts>()
                        .setQuery(firebasePostDB, UsersPosts.class)
                        .build();

        adapter = new HomeRecyclerViewAdapter(options);

        rvHomePosts.setAdapter(adapter);

        btnAddNewPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(view.getContext(), CreatePost.class), 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("-----------Home--", "onStart");
        adapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        Log.d("-----------Home--", "onStop");
    }

    private void init() {

        rvHomePosts = view.findViewById(R.id.rvHomePosts);
        btnAddNewPost = view.findViewById(R.id.btnAddNewPost);
        postsMap = new HashMap<>();
        finale = new ArrayList<>();
        firebasePostDB = FirebaseDatabase.getInstance().getReference().child("Post");
        firebaseUserDB = FirebaseDatabase.getInstance().getReference().child("User");
    }
}