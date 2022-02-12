package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class mobileNumber extends AppCompatActivity {

    private EditText mobileNumber_phoneNumber_et, mobileNumber_otp_et;
    private CardView mobileNumber_verify_cv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);

        mobileNumber_phoneNumber_et = (EditText) findViewById(R.id.mobileNumber_phoneNumber_et);
        mobileNumber_otp_et = (EditText) findViewById(R.id.mobileNumber_otp_et);
        mobileNumber_verify_cv = (CardView) findViewById(R.id.mobileNumber_verify_cv);


        String phoneNumber = mobileNumber_phoneNumber_et.getText().toString();


        mobileNumber_verify_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phoneNumber.isEmpty()) {

                    Intent intent = new Intent();
                    intent.putExtra("phoneNumber", mobileNumber_phoneNumber_et.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();

                }else{
                    Toast.makeText(mobileNumber.this, "Please Enter Your Phone Number", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}