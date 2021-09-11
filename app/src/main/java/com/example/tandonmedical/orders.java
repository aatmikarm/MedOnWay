package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class orders extends AppCompatActivity implements cartProductInterface {

    private RecyclerView cartProductRecyclerView;
    private String currentUserUid;
    private TextView cartTotalAmount;
    private CardView cart_buy_cv;

    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private ArrayList<productModelList> productModelLists;

    float totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().hide();


        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        cartTotalAmount = findViewById(R.id.cartTotalAmount);
        cart_buy_cv = findViewById(R.id.cart_buy_cv);

        productModelLists = getCartProducts();



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                cartProductRecyclerView = findViewById(R.id.cart_list_recycler_view);
                cartProductAdapter cartProductAdapter = new cartProductAdapter(getApplicationContext(), productModelLists, orders.this);
                cartProductRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                cartProductRecyclerView.setAdapter(cartProductAdapter);

            }
        }, 3000);


        cart_buy_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalAmount==0){

                    Toast.makeText(orders.this, "CART IS EMPTY", Toast.LENGTH_SHORT).show();

                }
                else if(totalAmount !=0 ){
                    Intent intent = new Intent(orders.this, payment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("paymentAmount",totalAmount);

                    startActivity(intent);

                }

            }
        });

    }


    private ArrayList<productModelList> getCartProducts() {

        final ArrayList<productModelList> productModelLists = new ArrayList<>();

        mDb.collection("users").document(currentUserUid).collection("orders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        totalAmount = totalAmount + Float.parseFloat((String) document.get("price"));;

                    }

                    cartTotalAmount.setText(String.valueOf(totalAmount));

                }
            }
        });

        return productModelLists;

    }

    @Override
    public void cartProductOnClickInterface(int position) {
        //Toast.makeText(orders.this, productModelLists.get(position).getName().toString() + " Removed from The Cart", Toast.LENGTH_SHORT).show();

        mDb.collection("users").document(currentUserUid).collection("orders")
                .document(productModelLists.get(position).getProductId().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(orders.this, productModelLists.get(position).getName().toString() + " Removed from The Cart", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), orders.class));

            }
        });

    }

}