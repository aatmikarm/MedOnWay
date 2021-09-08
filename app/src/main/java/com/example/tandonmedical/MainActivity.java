package com.example.tandonmedical;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView orders_history, user_profile_iv;
    private TextView username_tv;
    private RecyclerView productRecyclerView, doctorRecyclerView, categoriesRecyclerView;
    private String currentUserUid;

    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        username_tv = findViewById(R.id.username_tv);
        orders_history = findViewById(R.id.History_orders_image_view);
        user_profile_iv = findViewById(R.id.user_profile_iv);

        setCurrentUserImage();

        mDb.collection("users").document(currentUserUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username_tv.setText(document.get("name").toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ArrayList<productModelList> productModelLists = getAllProducts();
        final ArrayList<doctorsModelList> doctorsModelLists = getAllDoctors();
        final ArrayList<categoriesModelList> categoriesModelLists = getAllCategories();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                productRecyclerView = findViewById(R.id.product_list_recycler_view);
                productAdapter productAdapter = new productAdapter(getApplicationContext(), productModelLists);
                productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                productRecyclerView.setAdapter(productAdapter);

                doctorRecyclerView = findViewById(R.id.doctors_recycler_view);
                doctorsAdapter doctorsAdapter = new doctorsAdapter(getApplicationContext(), doctorsModelLists);
                doctorRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                doctorRecyclerView.setAdapter(doctorsAdapter);

                categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
                categoriesAdapter categoriesAdapter = new categoriesAdapter(getApplicationContext(), categoriesModelLists);
                categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                categoriesRecyclerView.setAdapter(categoriesAdapter);


            }
        }, 3000);

        orders_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), orders.class));
            }
        });
        user_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.tandonmedical.profile.class));
            }
        });

    }


    private ArrayList<productModelList> getAllProducts() {

        final ArrayList<productModelList> productModelLists = new ArrayList<>();

        mDb.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //it performs a for loop to get each seperate user details and location
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        productModelList productModelList = new productModelList();

                        productModelList.setName((String) document.get("name"));
                        productModelList.setImageUrl((String) document.get("imageUrl"));
                        productModelList.setPrice((String) document.get("price"));
                        productModelList.setDiscount((String) document.get("discount"));
                        productModelList.setMrp((String) document.get("mrp"));
                        productModelList.setCategory((String) document.get("category"));
                        productModelList.setProductId((String) document.get("productId"));
                        productModelList.setDescription((String) document.get("description"));

                        productModelLists.add(productModelList);

                    }
                }
            }
        });

        return productModelLists;
    }


    private ArrayList<doctorsModelList> getAllDoctors() {

        final ArrayList<doctorsModelList> doctorsModelLists = new ArrayList<>();

        mDb.collection("doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //it performs a for loop to get each seperate user details and location
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        doctorsModelList doctorsModelList = new doctorsModelList();

                        doctorsModelList.setName((String) document.get("name"));
                        doctorsModelList.setImageUrl((String) document.get("image"));
                        doctorsModelLists.add(doctorsModelList);

                    }
                }
            }
        });

        return doctorsModelLists;
    }

    private ArrayList<categoriesModelList> getAllCategories() {

        final ArrayList<categoriesModelList> categoriesModelLists = new ArrayList<>();

        mDb.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //it performs a for loop to get each seperate user details and location
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        categoriesModelList categoriesModelList = new categoriesModelList();

                        categoriesModelList.setName((String) document.get("name"));
                        categoriesModelList.setImageUrl((String) document.get("image"));
                        categoriesModelLists.add(categoriesModelList);

                    }
                }
            }
        });

        return categoriesModelLists;
    }

    private void setCurrentUserImage() {

        StorageReference ref = mStorageRef.child("images/" + currentUserUid).child("profilepic.jpg");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(user_profile_iv);
            }
        });
    }

}