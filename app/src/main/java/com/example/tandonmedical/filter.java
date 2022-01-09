package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class filter extends AppCompatActivity {

    private Chip chipRating4, chipRating3, chipRating2, chipRating1, chipHtoL, chipLtoH;
    private ChipGroup cgSort, cgPrice;
    private Button filter_apply;

    private String filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        chipRating4 = findViewById(R.id.chipRating4);
        chipRating3 = findViewById(R.id.chipRating3);
        chipRating2 = findViewById(R.id.chipRating2);
        chipRating1 = findViewById(R.id.chipRating1);
        chipHtoL = findViewById(R.id.chipHtoL);
        chipLtoH = findViewById(R.id.chipLtoH);
        cgSort = findViewById(R.id.cgSort);
        cgPrice = findViewById(R.id.cgPrice);
        filter_apply = findViewById(R.id.filter_apply);

        filter = "";

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter = buttonView.getText().toString();
                } else {
                    filter = "";
                }

            }
        };

        chipRating4.setOnCheckedChangeListener(checkedChangeListener);
        chipRating3.setOnCheckedChangeListener(checkedChangeListener);
        chipRating2.setOnCheckedChangeListener(checkedChangeListener);
        chipRating1.setOnCheckedChangeListener(checkedChangeListener);
        chipHtoL.setOnCheckedChangeListener(checkedChangeListener);
        chipLtoH.setOnCheckedChangeListener(checkedChangeListener);

        filter_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("filter", filter);
                setResult(101, resultIntent);
                finish();
            }
        });

    }
}