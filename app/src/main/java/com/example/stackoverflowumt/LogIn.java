package com.example.stackoverflowumt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LogIn extends AppCompatActivity {

    HashMap<String, String> map;
    TextInputLayout tilUsername, tilPassword;
    Button btnLogIn, btnForgotPassword, btnNewUser;
    ProgressBar pbLogIn;
    TextView tvWelcome, tvContinue;
    String username, password;
    DatabaseReference firebaseUserDB;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_log_in);

        init();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnLogIn.setVisibility(View.GONE);
                pbLogIn.setVisibility(View.VISIBLE);

                if (tilUsername.getEditText().getText().toString().isEmpty() ||
                        tilPassword.getEditText().getText().toString().isEmpty())
                    Toast.makeText(LogIn.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                else {

                    username = tilUsername.getEditText().getText().toString().trim();
                    password = tilPassword.getEditText().getText().toString().trim();

                    /*****************************************************************/
                    /**                         AUTH CODE                           **/
                    /*****************************************************************/

                    String email = username + "@umt.edu.pk";

                    Log.d("------Email------", "" + email);

                    fAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        FirebaseUser user = fAuth.getCurrentUser();
                                        if (user.isEmailVerified())
                                            readData(user);
                                        else {

                                            Toast.makeText(LogIn.this, "Account is not verified", Toast.LENGTH_SHORT).show();
                                            btnLogIn.setVisibility(View.VISIBLE);
                                            pbLogIn.setVisibility(View.GONE);
                                        }
                                    } else {

                                        Toast.makeText(LogIn.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                                        pbLogIn.setVisibility(View.GONE);
                                        btnLogIn.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });

        tilPassword.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                tilPassword.getEditText().setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        tilUsername.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                tilUsername.getEditText().setTextColor(getApplication().getResources().getColor(R.color.black));
            }
        });

        btnNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogIn.this, SignUp.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(tvWelcome, "transWelcome");
                pairs[1] = new Pair<View, String>(tvContinue, "transContinue");
                pairs[2] = new Pair<View, String>(tilUsername, "transUsername");
                pairs[3] = new Pair<View, String>(tilPassword, "transPassword");
                pairs[4] = new Pair<View, String>(btnLogIn, "transGO");
                pairs[5] = new Pair<View, String>(btnNewUser, "transOtherScreen");
                pairs[6] = new Pair<View, String>(btnForgotPassword, "transButton");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogIn.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(LogIn.this, "Reset Link Sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(LogIn.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void readData(FirebaseUser user) {

        firebaseUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data: snapshot.getChildren()) {

                    if (snapshot.exists() && data.getKey().equals(user.getUid())) {

                        map.put("Full Name", data.child("Full Name").getValue().toString().trim());
                        map.put("Fields", data.child("Fields").getValue().toString().trim());
                        map.put("Email", data.child("Email").getValue().toString().trim());
                        map.put("Username", username);

                        ApplicationClass.userMap = map;

                        Log.d("---------userMap------", "" + ApplicationClass.userMap);

                        startActivity(new Intent(LogIn.this, Home.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LogIn.this, "Sorry, an error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {

        map = new HashMap<>();
        tvWelcome = findViewById(R.id.tvWelcome);
        tvContinue = findViewById(R.id.tvContinue);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setVisibility(View.VISIBLE);
        btnNewUser = findViewById(R.id.btnNewUser);
        pbLogIn = findViewById(R.id.pbLogIn);
        pbLogIn.setVisibility(View.GONE);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        firebaseUserDB = FirebaseDatabase.getInstance().getReference().child("User");
        fAuth = FirebaseAuth.getInstance();
    }
}