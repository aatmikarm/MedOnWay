package com.example.tandonmedical;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class search extends AppCompatActivity implements searchProductInterface {

    private EditText search_et;
    private ImageView search_back_iv, search_filter_iv;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid, filter;
    private ProgressBar search_progress_bar;
    private RecyclerView searchProductRV;
    private searchProductAdapter searchProductAdapter;
    private ArrayList<productModelList> productModelLists;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();
        search_et = findViewById(R.id.search_et);
        search_back_iv = findViewById(R.id.search_back_iv);
        search_progress_bar = findViewById(R.id.search_progress_bar);
        search_filter_iv = findViewById(R.id.search_filter_iv);
        searchProductRV = findViewById(R.id.search_rv);
        searchProductRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        filter = "";

        search_et.setVisibility(View.GONE);

        if (filter.isEmpty()) {
            search_progress_bar.setVisibility(View.VISIBLE);
            productModelLists = getAllProducts(filter);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchProductAdapter = new searchProductAdapter(getApplicationContext(), productModelLists, search.this);
                searchProductRV.setAdapter(searchProductAdapter);
                search_progress_bar.setVisibility(View.GONE);
                search_et.setVisibility(View.VISIBLE);
            }
        }, 3000);

        search_et.addTextChangedListener(new TextWatcher() {
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


        search_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_filter_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(search.this, filter.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 101) {
            filter = data.getStringExtra("filter");
            productModelLists.clear();
            searchProductAdapter.notifyDataSetChanged();

            search_progress_bar.setVisibility(View.VISIBLE);
            search_et.setVisibility(View.GONE);
            productModelLists = getAllProducts(filter);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchProductAdapter = new searchProductAdapter(getApplicationContext(), productModelLists, search.this);
                    searchProductRV.setAdapter(searchProductAdapter);
                    search_progress_bar.setVisibility(View.GONE);
                    search_et.setVisibility(View.VISIBLE);
                }
            }, 3000);
        }
    }

    private ArrayList<productModelList> getAllProducts(String filter) {

        final ArrayList<productModelList> productModelLists = new ArrayList<>();
        if (filter.isEmpty()) {
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
                            productModelList.setPrescription((Boolean) document.get("prescription"));
                            productModelList.setRating((String) document.get("rating"));
                            productModelList.setReview((String) document.get("review"));
                            productModelList.setAvgRating((String) document.get("avgRating"));
                            productModelList.setSellerToken((String) document.get("sellerToken"));
                            productModelLists.add(productModelList);
                        }
                    }
                }
            });
        }
        if (!filter.isEmpty()) {
            if (filter.contains("High To Low")) {
                mDb.collection("products").orderBy("price", DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                productModelList productModelList = new productModelList();
                                Toast.makeText(search.this, (String) document.get("name"), Toast.LENGTH_SHORT).show();
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
                                productModelList.setPrescription((Boolean) document.get("prescription"));
                                productModelList.setRating((String) document.get("rating"));
                                productModelList.setReview((String) document.get("review"));
                                productModelList.setAvgRating((String) document.get("avgRating"));
                                productModelList.setSellerToken((String) document.get("sellerToken"));
                                productModelLists.add(productModelList);
                            }
                        }
                    }
                });
            }
            if (filter.contains("Low To High")) {
                mDb.collection("products").orderBy("price").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                productModelList.setPrescription((Boolean) document.get("prescription"));
                                productModelList.setRating((String) document.get("rating"));
                                productModelList.setReview((String) document.get("review"));
                                productModelList.setAvgRating((String) document.get("avgRating"));
                                productModelList.setSellerToken((String) document.get("sellerToken"));
                                productModelLists.add(productModelList);
                            }
                        }
                    }
                });
            }
            if (filter.contains("4.0") || filter.contains("3.0") || filter.contains("2.0") || filter.contains("1.0")) {
                mDb.collection("products").whereGreaterThanOrEqualTo("avgRating", filter).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                productModelList.setPrescription((Boolean) document.get("prescription"));
                                productModelList.setRating((String) document.get("rating"));
                                productModelList.setReview((String) document.get("review"));
                                productModelList.setAvgRating((String) document.get("avgRating"));
                                productModelList.setSellerToken((String) document.get("sellerToken"));
                                productModelLists.add(productModelList);
                            }
                        }
                    }
                });
            }
        }
        return productModelLists;
    }

    private void filter(String text) {
        ArrayList<productModelList> filteredList = new ArrayList<>();
        for (productModelList item : productModelLists) {
            String searchRating = item.name.toLowerCase();
            if (!searchRating.isEmpty() && searchRating.contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        searchProductAdapter.filterList(filteredList);
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
        intent.putExtra("prescription", productModelLists.get(position).getPrescription());
        intent.putExtra("discount", productModelLists.get(position).getDiscount());
        intent.putExtra("imageUrl", productModelLists.get(position).getImageUrl());
        intent.putExtra("category", productModelLists.get(position).getCategory());
        startActivity(intent);
    }
}