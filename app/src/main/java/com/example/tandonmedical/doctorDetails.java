package com.example.tandonmedical;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class doctorDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        getSupportActionBar().hide();
    }
}