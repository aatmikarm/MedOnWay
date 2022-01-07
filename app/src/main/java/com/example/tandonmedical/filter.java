package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class filter extends AppCompatActivity {

    private Chip chipRating, chipHtoL, chipLtoH;
    private ChipGroup cgSort, cgPrice;
    private Button filter_apply;

    private ArrayList<String> selectedChipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        chipRating = findViewById(R.id.chipRating);
        chipHtoL = findViewById(R.id.chipHtoL);
        chipLtoH = findViewById(R.id.chipLtoH);
        cgSort = findViewById(R.id.cgSort);
        cgPrice = findViewById(R.id.cgPrice);
        filter_apply = findViewById(R.id.filter_apply);

        selectedChipData = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedChipData.add(buttonView.getText().toString());
                }
                else {
                    selectedChipData.remove(buttonView.getText().toString());
                }

            }
        };

        chipRating.setOnCheckedChangeListener(checkedChangeListener);
        chipHtoL.setOnCheckedChangeListener(checkedChangeListener);
        chipLtoH.setOnCheckedChangeListener(checkedChangeListener);

        filter_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data",selectedChipData.toString());
                setResult(101,resultIntent);
                finish();
            }
        });

    }
}