package com.example.tandonmedical;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class doctorDetails extends AppCompatActivity {

    private ArrayList<doctorsModelList> doctorsModelLists;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid, name, email, password,
            bio, imageUrl, category, address, doctorId, phone,
            experience, status, rating, review, doctorToken,
            education, hospital, degree, timing, fee, tag, sign;
    private GeoPoint geo_point;
    private TextView doctorDetails_doctorName_tv, doctorDetails_category_tv, rating_and_review_star_tv,
            doctor_education, doctor_hospital, doctor_degree,doctor_fee;
    private ImageView doctor_profile_iv,doctorDetail_back_iv;
    private RatingBar rating_and_review_star_rb;
    private CardView doctorDetails_appointment_cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        doctorDetails_doctorName_tv = findViewById(R.id.doctorDetails_doctorName_tv);
        doctor_profile_iv = findViewById(R.id.doctor_profile_iv);
        doctorDetails_category_tv = findViewById(R.id.doctorDetails_category_tv);
        rating_and_review_star_rb = findViewById(R.id.rating_and_review_star_rb);
        rating_and_review_star_tv = findViewById(R.id.rating_and_review_star_tv);
        doctor_education = findViewById(R.id.doctor_education);
        doctor_hospital = findViewById(R.id.doctor_hospital);
        doctor_degree = findViewById(R.id.doctor_degree);
        doctor_fee = findViewById(R.id.doctor_fee);
        doctorDetails_appointment_cv = findViewById(R.id.doctorDetails_appointment_cv);
        doctorDetail_back_iv = findViewById(R.id.doctorDetail_back_iv);

        if (getIntent().getExtras() != null) {
            this.name = (String) getIntent().getExtras().get("name");
            this.email = (String) getIntent().getExtras().get("email");
            this.password = (String) getIntent().getExtras().get("password");
            this.bio = (String) getIntent().getExtras().get("bio");
            this.imageUrl = (String) getIntent().getExtras().get("imageUrl");
            this.category = (String) getIntent().getExtras().get("category");
            this.address = (String) getIntent().getExtras().get("address");
            this.doctorId = (String) getIntent().getExtras().get("doctorId");
            this.phone = (String) getIntent().getExtras().get("phone");
            this.experience = (String) getIntent().getExtras().get("experience");
            this.status = (String) getIntent().getExtras().get("status");
            this.rating = (String) getIntent().getExtras().get("rating");
            this.review = (String) getIntent().getExtras().get("review");
            this.doctorToken = (String) getIntent().getExtras().get("doctorToken");
            this.education = (String) getIntent().getExtras().get("education");
            this.hospital = (String) getIntent().getExtras().get("hospital");
            this.degree = (String) getIntent().getExtras().get("degree");
            this.timing = (String) getIntent().getExtras().get("timing");
            this.fee = (String) getIntent().getExtras().get("fee");
            this.tag = (String) getIntent().getExtras().get("tag");
            this.sign = (String) getIntent().getExtras().get("sign");
            this.geo_point = (GeoPoint) getIntent().getExtras().get("geo_point");
            //Toast.makeText(getApplicationContext(), doctorId, Toast.LENGTH_SHORT).show();
        }
        getDoctorDetails();

        doctorDetail_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDoctorDetails() {
        mDb.collection("doctors").document(doctorId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        setCurrentUserImage();
                        doctorDetails_doctorName_tv.setText((String) document.get("name").toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setCurrentUserImage() {
        StorageReference ref = mStorageRef.child("images/" + doctorId).child("profilepic.jpg");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(doctor_profile_iv);
            }
        });
    }
}