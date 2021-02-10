package com.example.stackoverflowumt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    View view;

    HashMap<String, String> map;
    String fullName, email, password, username, fields;
    ArrayList<String> fieldsList;
    TextView tvFullName, tvUsername;
    Button btnAddMoreTags, btnDoneEditing;
    TextInputLayout tilLockEmail, tilEnterNewPassword, tilConfirmNewPassword;
    ImageView ivEdit, ivLogOut;
    ArrayList<Chip> tags;
    int tagCount = 0;
    final int TAGS = 1;
    DatabaseReference firebaseDB;
    FirebaseAuth fAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        loadData();

        tilEnterNewPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {

            MaterialTextView label = view.findViewById(R.id.labelNewPass);
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus)
                    label.setTextColor(getResources().getColor(R.color.Matterhorn));
                else
                    label.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
            }
        });

        tilConfirmNewPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {

            MaterialTextView label = view.findViewById(R.id.labelConfirmPass);
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (tilEnterNewPassword.getEditText().getText().toString().isEmpty())
                    tilEnterNewPassword.requestFocus();
                else if (tilEnterNewPassword.getEditText().getText().toString().length() < 9) {

                    tilEnterNewPassword.requestFocus();
                    tilEnterNewPassword.getEditText().setError("Please enter at least 9 entities.");
                }
                else if (hasFocus)
                    label.setTextColor(getResources().getColor(R.color.Matterhorn));
                else
                    label.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
            }
        });

        tilConfirmNewPassword.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (s.toString().equals(tilEnterNewPassword.getEditText().getText().toString().trim())) {

                    password = tilConfirmNewPassword.getEditText().getText().toString().trim();
                    tilConfirmNewPassword.getEditText().setTextColor(getResources().getColor(R.color.black));
                }
                else
                    tilConfirmNewPassword.getEditText().setTextColor(getResources().getColor(R.color.design_default_color_error));
            }
        });

        ivLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fAuth.signOut();
                startActivity(new Intent(view.getContext(), LogIn.class));
                getActivity().finish();
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View view = getLayoutInflater().inflate(R.layout.change_password, null);
                TextInputLayout tilEnterOldPassword = view.findViewById(R.id.tilEnterOldPassword);

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Matterhorn));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Matterhorn));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!tilEnterOldPassword.getEditText().getText().toString().isEmpty()) {

                            AuthCredential credential = EmailAuthProvider.getCredential(email, tilEnterOldPassword.getEditText().getText().toString());
                            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            alertDialog.dismiss();
                                            tilEnterNewPassword.getEditText().setFocusableInTouchMode(true);
                                            tilEnterNewPassword.setBoxBackgroundColor(getResources().getColor(R.color.Albescent_White));

                                            tilConfirmNewPassword.getEditText().setFocusableInTouchMode(true);
                                            tilConfirmNewPassword.setBoxBackgroundColor(getResources().getColor(R.color.Albescent_White));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(view.getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                            tilEnterOldPassword.getEditText().setText("");
                                        }
                                    });
                        }
                        else
                            Toast.makeText(view.getContext(), "Enter Password first", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnAddMoreTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(view.getContext(), ShowTags.class);

                if (!fields.isEmpty())
                    intent.putExtra("Fields", fieldsList);
                startActivityForResult(intent, TAGS);
            }
        });

        btnDoneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (!password.equals("") && !fieldsList.isEmpty()) {
                    if (tilConfirmNewPassword.getEditText().getText().toString().trim().equals(tilEnterNewPassword.getEditText().getText().toString().trim())) {

                        user.updatePassword(password);
                        updateData(user);
                    }
                    else
                        Toast.makeText(view.getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                }

                else if (password.equals("") && !fieldsList.isEmpty())
                    updateData(user);

                else if (!password.equals("") && fieldsList.isEmpty()) {
                    if (tilConfirmNewPassword.getEditText().getText().toString().trim().equals(tilEnterNewPassword.getEditText().getText().toString().trim()))
                        user.updatePassword(password);
                    else
                        Toast.makeText(view.getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                }

                else
                    Toast.makeText(view.getContext(),"Data Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAGS && resultCode == RESULT_OK) {

            fieldsList = data.getStringArrayListExtra("Fields");
            showTags();
        }
    }

    private void updateData(FirebaseUser user) {

        map.put("Full Name", fullName);
        map.put("Email", email);
        map.put("Username", username);

        StringBuffer sb = new StringBuffer();

        for (String s : fieldsList) {
            sb.append(s);
            sb.append(",");
        }
        fields = sb.toString();
        fields = fields.substring(0, fields.length() - 1);

        map.put("Fields", fields);

        firebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data: snapshot.getChildren()) {

                    if (data.getKey().equals(user.getUid()))
                        firebaseDB.child(user.getUid()).child("Fields")
                                .setValue(fields)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(view.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                                        ApplicationClass.userMap = map;
                                    }
                                });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(view.getContext(), "Sorry, an error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {

        tvFullName.setText(fullName);
        tvUsername.setText(username);
        tilLockEmail.getEditText().setText(email);

        showTags();
    }

    private void showTags() {

        tagCount = fieldsList.size();

        for (int i = 0; i < tags.size(); i++) {

            Chip chip = tags.get(i);

            if (fieldsList.contains(chip.getText().toString().trim())) {

                chip.setVisibility(View.VISIBLE);
                chip.setCloseIconVisible(true);

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tagCount--;

                        if (tagCount <= 0)
                            Toast.makeText(view.getContext(), "You cannot delete the last field", Toast.LENGTH_SHORT).show();

                        else {

                            fieldsList.remove(chip.getText());
                            chip.setVisibility(View.GONE);
                        }
                    }
                });
            }
            else
                chip.setVisibility(View.GONE);
        }
    }

    private void init() {

        map = ApplicationClass.userMap;

        fullName = map.get("Full Name");
        email = map.get("Email");
        username = map.get("Username");
        fields = map.get("Fields");
        password = "";

        fieldsList = new ArrayList<>(Arrays.asList(fields.split(",")));

        tvFullName = view.findViewById(R.id.tvFullName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tilLockEmail = view.findViewById(R.id.tilLockEmail);
        tilEnterNewPassword = view.findViewById(R.id.tilEnterNewPassword);
        tilConfirmNewPassword = view.findViewById(R.id.tilConfirmNewPassword);
        ivEdit = view.findViewById(R.id.ivEdit);
        ivLogOut = view.findViewById(R.id.ivLogOut);
        btnAddMoreTags = view.findViewById(R.id.btnAddMoreTags);
        btnDoneEditing = view.findViewById(R.id.btnDoneEditing);

        tags = new ArrayList<>();
        tags.add(view.findViewById(R.id.tagAndroid));
        tags.add(view.findViewById(R.id.tagDb));
        tags.add(view.findViewById(R.id.tagIphone));
        tags.add(view.findViewById(R.id.tagPf));
        tags.add(view.findViewById(R.id.tagOop));
        tags.add(view.findViewById(R.id.tagGame));
        tags.add(view.findViewById(R.id.tagItc));
        tags.add(view.findViewById(R.id.tagDsa));

        firebaseDB = FirebaseDatabase.getInstance().getReference().child("User");
        fAuth = FirebaseAuth.getInstance();
    }
}