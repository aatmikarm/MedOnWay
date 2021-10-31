package com.example.tandonmedical;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class productDetails extends AppCompatActivity {

    private TextView productDetails_category_tv, productDetails_productName_tv, profile_no_of_prescriptions, productDetails_productDescription_tv;
    private String checkToLoop,currentUserUid,category, productId, description, discount, imageUrl, mrp, name, price, sellerId, seller, productUserStatus;
    private TextView productDetails_productRating_tv, productDetails_discount_tv, productDetails_mrp_tv, productDetails_price_tv, productDetails_quantity_tv;
    private CardView productDetails_minus_cv, productDetails_plus_cv, productDetails_quantity_cv, productDetails_addToCart_cv;
    private ImageView productDetails_image_iv, productDetail_back_iv, productDetail_cart_iv;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    int productQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().hide();

        productDetails_productName_tv = findViewById(R.id.productDetails_productName_tv);
        productDetails_category_tv = findViewById(R.id.productDetails_category_tv);
        productDetails_image_iv = findViewById(R.id.productDetails_image_iv);
        productDetails_price_tv = findViewById(R.id.productDetails_price_tv);
        productDetails_mrp_tv = findViewById(R.id.productDetails_mrp_tv);
        productDetails_discount_tv = findViewById(R.id.productDetails_discount_tv);
        productDetails_quantity_tv = findViewById(R.id.productDetails_quantity_tv);
        productDetails_productDescription_tv = findViewById(R.id.productDetails_productDescription_tv);
        productDetails_productRating_tv = findViewById(R.id.productDetails_productRating_tv);
        productDetails_addToCart_cv = findViewById(R.id.productDetails_addToCart_cv);
        productDetails_minus_cv = findViewById(R.id.productDetails_minus_cv);
        productDetails_plus_cv = findViewById(R.id.productDetails_plus_cv);
        productDetails_quantity_cv = findViewById(R.id.productDetails_quantity_cv);
        productDetail_cart_iv = findViewById(R.id.productDetail_cart_iv);
        productDetail_back_iv = findViewById(R.id.productDetail_back_iv);

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        if (getIntent().getExtras() != null) {
            this.category = (String) getIntent().getExtras().get("category");
            this.productId = (String) getIntent().getExtras().get("productId");
            this.description = (String) getIntent().getExtras().get("description");
            this.discount = (String) getIntent().getExtras().get("discount");
            this.imageUrl = (String) getIntent().getExtras().get("imageUrl");
            this.mrp = (String) getIntent().getExtras().get("mrp");
            this.name = (String) getIntent().getExtras().get("name");
            this.price = (String) getIntent().getExtras().get("price");
            this.sellerId = (String) getIntent().getExtras().get("sellerId");
            this.seller = (String) getIntent().getExtras().get("seller");
        }

        productDetails_productName_tv.setText(name);
        productDetails_category_tv.setText(category);
        productDetails_price_tv.setText(price + " Rs");
        productDetails_mrp_tv.setText(mrp + " Rs");
        productDetails_mrp_tv.setPaintFlags(productDetails_mrp_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productDetails_discount_tv.setText("GET " + discount + "% OFF");
        productDetails_productDescription_tv.setText(description);
        Glide.with(this).load(imageUrl).into(productDetails_image_iv);

        productDetails_minus_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productQuantity == 1) {
                    Toast.makeText(productDetails.this, "Least Quantity Limit", Toast.LENGTH_SHORT).show();
                    productDetails_quantity_tv.setText(String.valueOf(productQuantity));
                }
                if (productQuantity > 1) {
                    productQuantity--;
                    productDetails_quantity_tv.setText(String.valueOf(productQuantity));
                }
            }
        });
        productDetails_plus_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productQuantity < 5) {
                    productQuantity++;
                    productDetails_quantity_tv.setText(String.valueOf(productQuantity));
                }
                if (productQuantity == 5) {
                    Toast.makeText(productDetails.this, "Max Quantity Limit", Toast.LENGTH_SHORT).show();
                }
            }
        });
        productDetails_addToCart_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkProductStatus();
            }
        });
        productDetail_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        productDetail_cart_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), cart.class));
            }
        });


    }

    private void checkProductStatus() {
        mDb.collection("users").document(currentUserUid).collection("orders")
                .document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        productUserStatus = document.get("status").toString();

                        switch (productUserStatus) {
                            case "delivered": {
                                Map<String, Object> order = new HashMap<>();
                                order.put("name", name);
                                order.put("userId", currentUserUid);
                                order.put("imageUrl", imageUrl);
                                order.put("mrp", mrp);
                                order.put("price", price);
                                order.put("discount", discount);
                                order.put("description", description);
                                order.put("productId", productId);
                                order.put("category", category);
                                order.put("sellerId", sellerId);
                                order.put("seller", seller);
                                //in the cart product status
                                order.put("status", "in cart");
                                order.put("quantity", "1");
                                order.put("boughtTimes", "2");
                                mDb.collection("users").document(currentUserUid)
                                        .collection("orders").document(productId)
                                        .set(order);
                                Toast.makeText(productDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), cart.class));
                                finish();
                                break;
                            }
                            case "in cart": {
                                Map<String, Object> order = new HashMap<>();
                                order.put("name", name);
                                order.put("userId", currentUserUid);
                                order.put("imageUrl", imageUrl);
                                order.put("mrp", mrp);
                                order.put("price", price);
                                order.put("discount", discount);
                                order.put("description", description);
                                order.put("productId", productId);
                                order.put("category", category);
                                order.put("sellerId", sellerId);
                                order.put("seller", seller);
                                //in the cart product status
                                order.put("status", "in cart");
                                order.put("quantity", "2");
                                mDb.collection("users").document(currentUserUid)
                                        .collection("orders").document(productId)
                                        .update(order);
                                Toast.makeText(productDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), cart.class));
                                finish();
                                break;
                            }
                            case "on the way": {
                                Map<String, Object> order = new HashMap<>();
                                order.put("name", name);
                                order.put("userId", currentUserUid);
                                order.put("imageUrl", imageUrl);
                                order.put("mrp", mrp);
                                order.put("price", price);
                                order.put("discount", discount);
                                order.put("description", description);
                                order.put("productId", productId);
                                order.put("category", category);
                                order.put("sellerId", sellerId);
                                order.put("seller", seller);
                                //in the cart product status
                                order.put("status", "in cart");
                                order.put("quantity", "1");
                                mDb.collection("users").document(currentUserUid)
                                        .collection("orders").document(productId)
                                        .update(order);
                                Toast.makeText(productDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), cart.class));
                                finish();
                                break;
                            }
                        }

                    } else {
                        Toast.makeText(productDetails.this, "not bought", Toast.LENGTH_SHORT).show();
                        Map<String, Object> order = new HashMap<>();
                        order.put("name", name);
                        order.put("userId", currentUserUid);
                        order.put("imageUrl", imageUrl);
                        order.put("mrp", mrp);
                        order.put("price", price);
                        order.put("discount", discount);
                        order.put("description", description);
                        order.put("productId", productId);
                        order.put("category", category);
                        order.put("sellerId", sellerId);
                        order.put("seller", seller);
                        //in the cart product status
                        order.put("status", "in cart");
                        order.put("quantity", "1");
                        mDb.collection("users").document(currentUserUid)
                                .collection("orders").document(productId)
                                .set(order);
                        Toast.makeText(productDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), cart.class));
                        finish();
                    }
                } else {
                    Toast.makeText(productDetails.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}