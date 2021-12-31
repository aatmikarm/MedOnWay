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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class allDoctors extends AppCompatActivity {

    private EditText products_et;
    private ImageView products_back_iv;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid;
    private RecyclerView searchProductRV;
    private searchProductAdapter searchProductAdapter;
    private ArrayList<productModelList> productModelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctors);
        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();
        products_et = findViewById(R.id.products_et);
        products_back_iv = findViewById(R.id.products_back_iv);
        searchProductRV = findViewById(R.id.search_rv);

        searchProductRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        productModelLists = getAllProducts();
        searchProductAdapter = new searchProductAdapter(getApplicationContext(), productModelLists, allProducts.this);
        searchProductRV.setAdapter(searchProductAdapter);

        products_et.addTextChangedListener(new TextWatcher() {
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

        products_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<productModelList> filteredList = new ArrayList<>();

        for (productModelList item : productModelLists) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        searchProductAdapter.filterList(filteredList);
    }

    private ArrayList<productModelList> getAllProducts() {
        final ArrayList<productModelList> productModelLists = new ArrayList<>();
        mDb.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        productModelList productModelList = new productModelList();
                        productModelList.setName((String) document.get("name"));
                        productModelList.setImageUrl((String) document.get("imageUrl"));
                        productModelList.setPrice((String) document.get("price"));
                        productModelList.setDiscount((String) document.get("discount"));
                        productModelList.setMrp((String) document.get("mrp"));
                        productModelList.setCategory((String) document.get("category"));
                        productModelList.setSellerId((String) document.get("sellerId"));
                        productModelList.setSeller((String) document.get("seller"));
                        productModelList.setProductId((String) document.get("productId"));
                        productModelList.setDescription((String) document.get("description"));
                        productModelList.setRating((String) document.get("rating"));
                        productModelList.setReview((String) document.get("review"));
                        productModelList.setSellerToken((String) document.get("sellerToken"));
                        productModelLists.add(productModelList);
                    }
                }
            }
        });
        return productModelLists;
    }

    @Override
    public void searchProductOnClickInterface(int position) {
        Intent intent = new Intent(getApplicationContext(), productDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sellerId", productModelLists.get(position).getSellerId());
        intent.putExtra("productId", productModelLists.get(position).getProductId());
        intent.putExtra("seller", productModelLists.get(position).getSeller());
        intent.putExtra("mrp", productModelLists.get(position).getMrp());
        intent.putExtra("name", productModelLists.get(position).getName());
        intent.putExtra("price", productModelLists.get(position).getPrice());
        intent.putExtra("description", productModelLists.get(position).getDescription());
        intent.putExtra("discount", productModelLists.get(position).getDiscount());
        intent.putExtra("imageUrl", productModelLists.get(position).getImageUrl());
        intent.putExtra("category", productModelLists.get(position).getCategory());
        startActivity(intent);
    }
}