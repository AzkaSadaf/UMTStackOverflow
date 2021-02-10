package com.example.stackoverflowumt;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ApplicationClass extends Application {

    public static HashMap<String, String> userMap;
    public static HashMap<String, String> userPosts;

    @Override
    public void onCreate() {
        super.onCreate();

        userMap = new HashMap<>();
        userPosts = new HashMap<>();

    }

    /**         DATA REGARDING LOGIN AND SIGNUP         **/

    /**         s2018065026     123456789               **/
    /**         f2017065133     qwertyuiop              **/
}
