package com.example.stackoverflowumt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    TextInputLayout tilFullName, tilSetPassword, tilConfirmPassword, tilEmail;
    TextView tvSetWelcome, tvSetContinue;
    MaterialTextView matTvUsername;
    Button btnSignUp, btnAlreadyAUser, btnSelectTag;
    ProgressBar pbSignUp;
    String fullName, email, userName, password;
    ArrayList<String> fieldsList;
    HashMap<String, String> map = new HashMap<>();
    DatabaseReference firebaseDB;
    private FirebaseAuth fAuth;
    boolean fine = false;
    final int TAGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        init();

        tilEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                boolean match = false;
                if (tilEmail.getEditText().getText() != null) {

                    if (Pattern.matches("[SsFf][0-9]{10}[@]umt[.]edu[.]pk", tilEmail.getEditText().getText().toString().trim())) {

                        email = tilEmail.getEditText().getText().toString().trim();
                        userName = email.split("@")[0];
                        matTvUsername.setText("Your Username is: " + userName);

                        map.put("Email", email);                                                    /**EMAIL ADDED TO HASHMAP**/
                        Log.d("Email", map.get("Email") + "");
                        map.put("Username", userName);                                              /**USERNAME ADDED TO HASHMAP**/
                        Log.d("Username", map.get("Username") + "");
                        fine = true;
                        match = true;
                    }
                    else
                        fine = false;
                }

                if (!hasFocus && !match) {

                    Toast.makeText(SignUp.this, "Register yourself using UMT email address.", Toast.LENGTH_SHORT).show();
                    tilEmail.getEditText().setError("Register yourself using UMT mail address.");
                    fine = false;
                }
            }
        });

        tilConfirmPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (tilSetPassword.getEditText().getText().toString().isEmpty())
                    tilSetPassword.requestFocus();
                else if (tilSetPassword.getEditText().getText().toString().length() < 9) {

                    tilSetPassword.requestFocus();
                    tilSetPassword.getEditText().setError("Please enter at least 9 entities.");
                }
            }
        });

        tilConfirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (s.toString().equals(tilSetPassword.getEditText().getText().toString().trim())) {

                    password = tilConfirmPassword.getEditText().getText().toString().trim();

                    tilConfirmPassword.getEditText().setTextColor(getApplication().getResources().getColor(R.color.black));
                }
                else
                    tilConfirmPassword.getEditText().setTextColor(getApplication().getResources().getColor(R.color.design_default_color_error));
            }
        });

        btnSelectTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this, ShowTags.class);
                if (!fieldsList.isEmpty()) {

                    btnSelectTag.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    intent.putExtra("Fields", fieldsList);

                    Log.d("btnSelectTag", "Not FAIR");
                }

                startActivityForResult(intent, TAGS);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //btnSignUp.setVisibility(View.GONE);
                //pbSignUp.setVisibility(View.GONE);

                if (tilFullName.getEditText().getText().toString().isEmpty() ||
                        tilEmail.getEditText().getText().toString().isEmpty() ||
                        tilSetPassword.getEditText().getText().toString().isEmpty() ||
                        tilConfirmPassword.getEditText().getText().toString().isEmpty() ||
                        fieldsList.isEmpty()) {

                    Log.d("---------Toast--------", "Please fill all fields");

                    Toast.makeText(SignUp.this, "Please fill all spaces provided and select a field", Toast.LENGTH_SHORT).show();
                    btnSignUp.setVisibility(View.VISIBLE);
                }
                else {


                    Log.d("-----------Fields------", "" + fieldsList);

                    if (!fine)
                    {
                        tilEmail.getEditText().setError("Register yourself using UMT mail address");
                        btnSignUp.setVisibility(View.GONE);
                    }
                    else if (!tilSetPassword.getEditText().getText().toString().trim().equals(password))
                    {
                        Toast.makeText(SignUp.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                        btnSignUp.setVisibility(View.GONE);
                    }
                    else if (fine && tilSetPassword.getEditText().getText().toString().trim().equals(password)) {
                        btnSignUp.setVisibility(View.GONE);
                        pbSignUp.setVisibility(View.VISIBLE);

                        fullName = tilFullName.getEditText().getText().toString().trim();

                        map.put("Full Name", fullName);                                             /**FULL NAME ADDED TO HASHMAP**/

                        Log.d("-----Full Name---------", map.get("Full Name") + "");

                        Log.d("--------Original-------", "" + fieldsList);

                        StringBuffer sb = new StringBuffer();

                        for (String s : fieldsList) {
                            sb.append(s);
                            sb.append(",");
                        }

                        String field = sb.toString();

                        Log.d("------String-------", "" + field);

                        field = field.substring(0, field.length() - 1);

                        map.put("Fields", field);                                                   /**FIELDS ADDED TO HASHMAP**/

                        /*****************************************************************/
                        /**                         AUTH CODE                           **/
                        /*****************************************************************/

                        fAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()) {

                                            FirebaseUser user = fAuth.getCurrentUser();
                                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    Toast.makeText(SignUp.this, "Verification Email has been sent",Toast.LENGTH_SHORT);
                                                }
                                            }).addOnFailureListener(new OnFailureListener()
                                            {
                                                @Override
                                                public void onFailure(@NonNull Exception e)
                                                {
                                                    Toast.makeText(SignUp.this, "Something went wrong",Toast.LENGTH_SHORT);

                                                }
                                            });

                                            Log.d("-------UID--------", "UID is " + map.get("UID"));

                                            firebaseDB.child(user.getUid())
                                                    .setValue(map)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(SignUp.this, "User Registered Successfully, Please Verify your account", Toast.LENGTH_SHORT).show();

                                                                Log.d("--------OnComplete-----", "Working");

                                                                btnSignUp.setVisibility(View.GONE);
                                                                pbSignUp.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    });

                                            ApplicationClass.userMap = map;
                                            startActivity(new Intent(SignUp.this, LogIn.class));
                                        }
                                        else {

                                            Toast.makeText(SignUp.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUp.this, SignUp.class));
                                        }
                                    }
                                });
                    }
                }
            }
        });

        btnAlreadyAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this, LogIn.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(tvSetWelcome, "transWelcome");
                pairs[1] = new Pair<View,String>(tvSetContinue, "transContinue");
                pairs[2] = new Pair<View,String>(tilEmail, "transUsername");
                pairs[3] = new Pair<View,String>(tilSetPassword, "transPassword");
                pairs[4] = new Pair<View,String>(btnSignUp, "transGO");
                pairs[5] = new Pair<View,String>(btnAlreadyAUser, "transOtherScreen");
                pairs[6] = new Pair<View,String>(btnSelectTag, "transButton");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                    startActivity(intent, options.toBundle());
                    //finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAGS && resultCode == RESULT_OK) {

            fieldsList = data.getStringArrayListExtra("Fields");
            Drawable image = ResourcesCompat.getDrawable(getResources(), R.drawable.selected, getApplication().getTheme());
            btnSelectTag.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null);
        }
    }

    private void init() {

        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        matTvUsername = findViewById(R.id.matTvUsername);
        tilSetPassword = findViewById(R.id.tilSetPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tvSetWelcome = findViewById(R.id.tvSetWelcome);
        tvSetContinue = findViewById(R.id.tvSetContinue);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setVisibility(View.VISIBLE);
        pbSignUp = findViewById(R.id.pbSignUp);
        pbSignUp.setVisibility(View.GONE);
        btnAlreadyAUser = findViewById(R.id.btnAlreadyAUser);
        btnSelectTag = findViewById(R.id.btnSelectTag);
        firebaseDB = FirebaseDatabase.getInstance().getReference().child("User");
        fAuth = FirebaseAuth.getInstance();
        fieldsList = new ArrayList();
    }
}