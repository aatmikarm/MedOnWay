package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class allDoctors extends AppCompatActivity {

    private EditText allDoctors_et;
    private ImageView allDoctors_back_iv;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid;
    private RecyclerView allDoctors_search_rv;
    private allDoctorsAdapter allDoctorsAdapter;
    private ArrayList<doctorsModelList> doctorsModelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctors);
        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();
        allDoctors_et = findViewById(R.id.allDoctors_et);
        allDoctors_back_iv = findViewById(R.id.allDoctors_back_iv);
        allDoctors_search_rv = findViewById(R.id.allDoctors_search_rv);

        allDoctors_search_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        doctorsModelLists = getAllDoctors();
        allDoctorsAdapter = new allDoctorsAdapter(getApplicationContext(), doctorsModelLists);
        allDoctors_search_rv.setAdapter(allDoctorsAdapter);

        allDoctors_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        allDoctors_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<doctorsModelList> filteredList = new ArrayList<>();

        for (doctorsModelList item : doctorsModelLists) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        allDoctorsAdapter.filterList(filteredList);
    }

    private ArrayList<doctorsModelList> getAllDoctors() {
        final ArrayList<doctorsModelList> doctorsModelLists = new ArrayList<>();
        mDb.collection("doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        doctorsModelList doctorsModelList = new doctorsModelList();
                        doctorsModelList.setName((String) document.get("name"));
                        doctorsModelList.setEmail((String) document.get("email"));
                        doctorsModelList.setPassword((String) document.get("password"));
                        doctorsModelList.setBio((String) document.get("bio"));
                        doctorsModelList.setImageUrl((String) document.get("imageUrl"));
                        doctorsModelList.setCategory((String) document.get("category"));
                        doctorsModelList.setAddress((String) document.get("address"));
                        doctorsModelList.setDoctorId((String) document.get("doctorId"));
                        doctorsModelList.setPhone((String) document.get("phone"));
                        doctorsModelList.setExperience((String) document.get("experience"));
                        doctorsModelList.setStatus((String) document.get("status"));
                        doctorsModelList.setRating((String) document.get("rating"));
                        doctorsModelList.setReview((String) document.get("review"));
                        doctorsModelList.setDoctorToken((String) document.get("doctorToken"));
                        doctorsModelList.setEducation((String) document.get("education"));
                        doctorsModelList.setHospital((String) document.get("hospital"));
                        doctorsModelList.setDegree((String) document.get("degree"));
                        doctorsModelList.setTiming((String) document.get("timing"));
                        doctorsModelList.setFee((String) document.get("fee"));
                        doctorsModelList.setTag((String) document.get("tag"));
                        doctorsModelList.setSign((String) document.get("sign"));
                        doctorsModelList.setGeo_point((GeoPoint) document.get("geo_point"));
                        doctorsModelLists.add(doctorsModelList);
                    }
                }
            }
        });
        return doctorsModelLists;
    }
}