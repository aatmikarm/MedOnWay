package com.example.tandonmedical;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private String category, imageUrl;
    private ImageView backBtn, specific_category_cart_iv;
    private TextView Category_name_tv, specific_category_cart_tv;
    private RecyclerView productRecyclerView;
    private ProgressBar specific_category_pb;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_category);

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        backBtn = findViewById(R.id.specificCategory_back_btn);
        specific_category_pb = findViewById(R.id.specific_category_pb);
        Category_name_tv = findViewById(R.id.specificCategory_name_tv);

        specific_category_cart_iv = findViewById(R.id.specific_category_cart_iv);
        specific_category_cart_tv = findViewById(R.id.specific_category_cart_tv);

        specific_category_pb.setVisibility(View.VISIBLE);
        specific_category_cart_iv.setVisibility(View.GONE);
        specific_category_cart_tv.setVisibility(View.GONE);

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

                specific_category_pb.setVisibility(View.GONE);
                productRecyclerView = findViewById(R.id.specific_category_rv);
                productAdapter productAdapter = new productAdapter(getApplicationContext(), productModelLists);
                // productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                productRecyclerView.setLayoutManager(new GridLayoutManager(specificCategory.this, 2));
                productRecyclerView.setAdapter(productAdapter);

            }
        }, 3000);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private ArrayList<productModelList> getSpecificProducts() {

        final ArrayList<productModelList> productModelLists = new ArrayList<>();

        mDb.collection("products").whereEqualTo("category", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    //it performs a for loop to get each seperate user details and location
                    specific_category_cart_iv.setVisibility(View.GONE);
                    specific_category_cart_tv.setVisibility(View.GONE);

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
                } else {
                    specific_category_cart_iv.setVisibility(View.VISIBLE);
                    specific_category_cart_tv.setVisibility(View.VISIBLE);
                }
            }
        });

        return productModelLists;
    }


}