package com.example.tandonmedical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class specificCategory extends AppCompatActivity {

    private String category,imageUrl;
    private ImageView backBtn;
    private TextView Category_name_tv;
    private RecyclerView productRecyclerView;

    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_category);

        getSupportActionBar().hide();


        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        backBtn = findViewById(R.id.specificCategory_back_btn);
        Category_name_tv = findViewById(R.id.specificCategory_name_tv);

        if (getIntent().getExtras() != null) {
            this.category = (String) getIntent().getExtras().get("category");
            this.imageUrl = (String) getIntent().getExtras().get("imageUrl");
        }

        Category_name_tv.setText(category);
        final ArrayList<productModelList> productModelLists = getSpecificProducts();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                productRecyclerView = findViewById(R.id.specific_category_rv);
                productAdapter productAdapter = new productAdapter(getApplicationContext(), productModelLists);
                productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                productRecyclerView.setAdapter(productAdapter);

            }
        }, 3000);

    }

    private ArrayList<productModelList> getSpecificProducts() {

        final ArrayList<productModelList> productModelLists = new ArrayList<>();

        mDb.collection("products").whereEqualTo("category",category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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



}