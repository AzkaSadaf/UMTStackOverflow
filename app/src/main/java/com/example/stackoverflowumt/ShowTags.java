package com.example.stackoverflowumt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ShowTags extends AppCompatActivity {

    Button btnDoneTag;
    int count = 0;
    ArrayList<String> fieldList;
    ArrayList<Chip> chips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tags);

        init();

        if (getIntent().getStringArrayListExtra("Fields") != null) {

            Log.d("--------Hoooo---------", "Intent is received");

            fieldList = getIntent().getStringArrayListExtra("Fields");

            Log.d("---------Hoooo--------", "" + fieldList.get(0));

            onReceivingIntent(fieldList);
        }

        btnDoneTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count > 0) {

                    Intent intent = new Intent();
                    intent.putExtra("Fields", fieldList);
                    setResult(RESULT_OK, intent);
                    ShowTags.this.finish();
                }
            }
        });
    }

    private void onReceivingIntent(ArrayList<String> compare) {

        count = compare.size();
        for (int i = 0; i < chips.size(); i++) {

            Chip chip = chips.get(i);
            if (compare.contains(chip.getText().toString().trim())) {

                Log.d("--------Count---------", "" + count);

                chip.setChecked(true);

                Log.d("------chip.getText()---", "" + chip.getText());

                btnDoneTag.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary_dark));
            }
        }
    }

    public void onTagSelected(View v) {

        Chip chipTag = findViewById(v.getId());

        if (chipTag.isChecked()) {

            Log.d("----Button Selected----", "" + chipTag.getText());

            count++;
            if (count > 0)
                btnDoneTag.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary_dark));
            fieldList.add(chipTag.getText().toString().trim());
        }
        else {

            Log.d("----Button Deselected--", "" + chipTag.getText());
            count--;
            if (count <= 0)
                btnDoneTag.setBackgroundColor(getResources().getColor(R.color.notClickable));
            fieldList.remove(chipTag.getText().toString().trim());
        }
    }

    private void init() {

        btnDoneTag = findViewById(R.id.btnPost);

        fieldList = new ArrayList<>();

        chips = new ArrayList<>();
        chips.add(findViewById(R.id.chipAndroid));
        chips.add(findViewById(R.id.chipDb));
        chips.add(findViewById(R.id.chipPf));
        chips.add(findViewById(R.id.chipOop));
        chips.add(findViewById(R.id.chipGame));
        chips.add(findViewById(R.id.chipIphone));
        chips.add(findViewById(R.id.chipItc));
        chips.add(findViewById(R.id.chipDsa));
    }
}