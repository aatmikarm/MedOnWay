package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class orders extends AppCompatActivity implements ordersProductInterface {

    private RecyclerView ordersProductRecyclerView;
    private TextView order_cart_tv;
    private ImageView orders_back_iv,order_cart_iv;
    private ProgressBar order_pb;
    private String sellerId;
    private String seller;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid;
    private ArrayList<productModelList> productModelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
      

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        orders_back_iv = findViewById(R.id.orders_back_iv);
        order_cart_iv = findViewById(R.id.order_cart_iv);
        order_cart_tv = findViewById(R.id.order_cart_tv);
        order_pb = findViewById(R.id.order_pb);
        productModelLists = getOrdersProducts();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                order_pb.setVisibility(View.GONE);
                ordersProductRecyclerView = findViewById(R.id.orders_list_recycler_view);
                ordersProductAdapter ordersProductAdapter = new ordersProductAdapter(getApplicationContext(), productModelLists, orders.this);
                ordersProductRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                ordersProductRecyclerView.setAdapter(ordersProductAdapter);
            }
        }, 3000);

        orders_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private ArrayList<productModelList> getOrdersProducts() {
        final ArrayList<productModelList> productModelLists = new ArrayList<>();
        mDb.collection("users").document(currentUserUid).collection("orders")
                .whereEqualTo("status", "on the way")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        order_cart_iv.setVisibility(View.GONE);
                        order_cart_tv.setVisibility(View.GONE);
                        productModelList productModelList = new productModelList();
                        productModelList.setName((String) document.get("name"));
                        productModelList.setImageUrl((String) document.get("imageUrl"));
                        productModelList.setPrice((String) document.get("price"));
                        productModelList.setDiscount((String) document.get("discount"));
                        productModelList.setMrp((String) document.get("mrp"));
                        productModelList.setSellerId((String) document.get("sellerId"));
                        productModelList.setSeller((String) document.get("seller"));
                        productModelList.setCategory((String) document.get("category"));
                        productModelList.setProductId((String) document.get("productId"));
                        productModelList.setProductOrderId((String) document.get("productOrderId"));
                        productModelList.setProductQuantity((String) document.get("productQuantity"));
                        productModelList.setProductOrderPlacedTime((String) document.get("productOrderPlacedTime"));
                        productModelList.setDescription((String) document.get("description"));
                        productModelList.setStatus((String) document.get("status"));
                        productModelLists.add(productModelList);
                    }
                }
            }
        });

        mDb.collection("users").document(currentUserUid).collection("orders")
                .whereEqualTo("status", "delivered")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        order_cart_iv.setVisibility(View.GONE);
                        order_cart_tv.setVisibility(View.GONE);
                        productModelList productModelList = new productModelList();
                        productModelList.setName((String) document.get("name"));
                        productModelList.setImageUrl((String) document.get("imageUrl"));
                        productModelList.setPrice((String) document.get("price"));
                        productModelList.setDiscount((String) document.get("discount"));
                        productModelList.setMrp((String) document.get("mrp"));
                        productModelList.setSellerId((String) document.get("sellerId"));
                        productModelList.setSeller((String) document.get("seller"));
                        productModelList.setCategory((String) document.get("category"));
                        productModelList.setProductId((String) document.get("productId"));
                        productModelList.setProductOrderId((String) document.get("productOrderId"));
                        productModelList.setProductQuantity((String) document.get("productQuantity"));
                        productModelList.setProductOrderPlacedTime((String) document.get("productOrderPlacedTime"));
                        productModelList.setDescription((String) document.get("description"));
                        productModelList.setStatus((String) document.get("status"));
                        productModelLists.add(productModelList);
                    }
                }
            }
        });
        return productModelLists;
    }

    @Override
    public void ordersProductOnClickInterface(int position) {
        finish();
        Intent intent = new Intent(getApplicationContext(), productStatus.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sellerId", productModelLists.get(position).getSellerId());
        intent.putExtra("productId", productModelLists.get(position).getProductId());
        intent.putExtra("productOrderId", productModelLists.get(position).getProductOrderId());
        intent.putExtra("seller", productModelLists.get(position).getSeller());
        startActivity(intent);
    }
}