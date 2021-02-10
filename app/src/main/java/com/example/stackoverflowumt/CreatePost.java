package com.example.stackoverflowumt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatePost extends AppCompatActivity {

    TextView txtName;
    TextInputLayout txtPostTitle, txtPostDescription;
    Button btnSelectPostTag, btnPost;
    ArrayList<String> fieldsList;
    ArrayList<Chip> postTags;
    final int TAGS = 1;
    int tagCount = 0;
    DatabaseReference firebaseUserDB;
    DatabaseReference firebasePostDB;
    HashMap<String, String> postMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_post);

        init();

        btnSelectPostTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(CreatePost.this, ShowTags.class), TAGS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAGS && resultCode == RESULT_OK) {

            fieldsList = data.getStringArrayListExtra("Fields");

            showTags();
        }
    }

    private void showTags() {

        tagCount = fieldsList.size();

        for (int i = 0; i < postTags.size(); i++) {

            Chip chip = postTags.get(i);

            if (fieldsList.contains(chip.getText().toString().trim())) {

                chip.setVisibility(View.VISIBLE);
                chip.setCloseIconVisible(true);

                Log.d("chip.getText()", "" + chip.getText());

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tagCount--;

                        if (tagCount <= 0)

                            Toast.makeText(CreatePost.this, "You cannot delete the last field", Toast.LENGTH_SHORT).show();
                        else {

                            Log.d("Chip Count", "" + tagCount);

                            fieldsList.remove(chip.getText());

                            Log.d("ArrayList", "" + fieldsList);

                            chip.setVisibility(View.GONE);
                        }
                    }
                });
            }
            else
                chip.setVisibility(View.GONE);
        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtPostTitle.getEditText().getText().toString().isEmpty() ||
                txtPostDescription.getEditText().getText().toString().isEmpty())
                    Toast.makeText(CreatePost.this, "Title and description are must for the post.", Toast.LENGTH_SHORT).show();

                else if (fieldsList.isEmpty())
                    Toast.makeText(CreatePost.this, "Please select a tag.", Toast.LENGTH_SHORT).show();

                else {

                    String postTitle = txtPostTitle.getEditText().getText().toString().trim();
                    String postDescription = txtPostDescription.getEditText().getText().toString().trim();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    postMap.put("Post_Title", postTitle);
                    postMap.put("Post_Description", postDescription);

                    StringBuffer sb = new StringBuffer();

                    for (String s : fieldsList) {
                        sb.append(s);
                        sb.append(",");
                    }

                    String field = sb.toString();

                    Log.d("String", "" + field);

                    field = field.substring(0, field.length() - 1);

                    postMap.put("Post_Tags", field);
                    postMap.put("Post_UID", user.getUid());
                    postMap.put("Post_IsResolved", "No");

                    postMap.put("Post_Username", ApplicationClass.userMap.get("Username"));
                    postMap.put("Post_UserFullName", ApplicationClass.userMap.get("Full Name"));

                    String pushedPostID = firebasePostDB.push().getKey();

                    firebasePostDB.child(pushedPostID)
                            .setValue(postMap)
                            .addOnFailureListener(new OnFailureListener() {

                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(CreatePost.this, "Sorry an error occurred", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(CreatePost.this, "Posted", Toast.LENGTH_SHORT).show();

                                        Log.d("OnComplete", "Working");

                                        firebaseUserDB.child(user.getUid()).child("Posts").child(pushedPostID).setValue(postMap.get("Post_Tags"));

                                        startActivity(new Intent(CreatePost.this, Home.class));

                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void init() {

        txtName = findViewById(R.id.txtName);
        txtPostTitle = findViewById(R.id.txtPostTitle);
        txtPostDescription = findViewById(R.id.txtPostDescription);
        btnSelectPostTag = findViewById(R.id.btnSelectPostTag);
        btnPost = findViewById(R.id.btnPost);

        fieldsList = new ArrayList<>();

        txtName.setText(ApplicationClass.userMap.get("Full Name"));

        postTags = new ArrayList<>();
        postTags.add(findViewById(R.id.postTagAndroid));
        postTags.add(findViewById(R.id.postTagDb));
        postTags.add(findViewById(R.id.postTagIphone));
        postTags.add(findViewById(R.id.postTagPf));
        postTags.add(findViewById(R.id.postTagOop));
        postTags.add(findViewById(R.id.postTagGame));
        postTags.add(findViewById(R.id.postTagItc));
        postTags.add(findViewById(R.id.postTagDsa));

        firebaseUserDB = FirebaseDatabase.getInstance().getReference().child("User");
        firebasePostDB = FirebaseDatabase.getInstance().getReference().child("Post");
        //firebaseDB.child("Posts").push().getKey();

        postMap = new HashMap<>();
    }
}