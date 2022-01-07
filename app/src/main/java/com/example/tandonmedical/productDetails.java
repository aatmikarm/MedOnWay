package com.example.tandonmedical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class productDetails extends AppCompatActivity {

    private FragmentManager ratingfragmentManager;
    private TextView productDetails_category_tv, productDetails_productName_tv, profile_no_of_prescriptions, productDetails_productDescription_tv;
    private String sellerToken, checkToLoop, currentUserUid, category, productId, description,
            discount, imageUrl, mrp, name, price, sellerId, seller, productUserStatus,
            prescriptionUrl,prescriptionId, rating, review;
    private TextView productDetails_discount_tv, productDetails_mrp_tv, productDetails_price_tv, productDetails_quantity_tv, productDetails_prescription_tv;
    private CardView productDetails_minus_cv, productDetails_plus_cv, productDetails_quantity_cv, productDetails_addToCart_cv;
    private ImageView productDetails_image_iv, productDetail_back_iv, productDetail_cart_iv;
    private Boolean prescription;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private int productQuantity = 1;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        productDetails_productName_tv = findViewById(R.id.productDetails_productName_tv);
        productDetails_category_tv = findViewById(R.id.productDetails_category_tv);
        productDetails_image_iv = findViewById(R.id.productDetails_image_iv);
        productDetails_price_tv = findViewById(R.id.productDetails_price_tv);
        productDetails_mrp_tv = findViewById(R.id.productDetails_mrp_tv);
        productDetails_discount_tv = findViewById(R.id.productDetails_discount_tv);
        productDetails_quantity_tv = findViewById(R.id.productDetails_quantity_tv);
        productDetails_productDescription_tv = findViewById(R.id.productDetails_productDescription_tv);
        productDetails_prescription_tv = findViewById(R.id.productDetails_prescription_tv);
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
            this.prescription = (Boolean) getIntent().getExtras().get("prescription");
            this.discount = (String) getIntent().getExtras().get("discount");
            this.imageUrl = (String) getIntent().getExtras().get("imageUrl");
            this.mrp = (String) getIntent().getExtras().get("mrp");
            this.name = (String) getIntent().getExtras().get("name");
            this.price = (String) getIntent().getExtras().get("price");
            this.sellerId = (String) getIntent().getExtras().get("sellerId");
            this.seller = (String) getIntent().getExtras().get("seller");
            this.rating = (String) getIntent().getExtras().get("rating");
            this.review = (String) getIntent().getExtras().get("review");
            this.sellerToken = (String) getIntent().getExtras().get("sellerToken");
        }

        productDetails_productName_tv.setText(name);
        productDetails_category_tv.setText(category);
        productDetails_price_tv.setText(price + " Rs");
        productDetails_mrp_tv.setText(mrp + " Rs");
        productDetails_mrp_tv.setPaintFlags(productDetails_mrp_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productDetails_discount_tv.setText("GET " + discount + "% OFF");
        productDetails_productDescription_tv.setText(description);
        Glide.with(this).load(imageUrl).into(productDetails_image_iv);

        if (savedInstanceState == null) {
            ratingFragment ratingFragment = new ratingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("productId", productId);
            ratingFragment.setArguments(bundle);
            ratingfragmentManager = getSupportFragmentManager();
            ratingfragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.rating_fcv, ratingFragment.class, bundle)
                    .commit();
        }

        if (prescription == true) {
            productDetails_prescription_tv.setVisibility(View.VISIBLE);
        }
        if (prescription == false) {
            productDetails_prescription_tv.setVisibility(View.INVISIBLE);
        }

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
                if (prescription == true) {
                    selectImage();
                }
                if (prescription == false) {
                    addProductToCart();
                }

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

    // Select Image method
    private void selectImage() {   // select image from photos in galary funtion
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 77);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 77 && resultCode == RESULT_OK && data != null && data.getData() != null) {   // Get the Uri of data
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //profile_iv.setImageBitmap(bitmap);
                try {
                    uploadPrescription();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPrescription() throws IOException {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Prescription...");
            progressDialog.show();
            Uri file = filePath;
            StorageReference tempRef = mStorageRef.child("prescription/" + file.getLastPathSegment());
            UploadTask uploadTask = tempRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    tempRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //setImageUrl(uri.toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("ssmmHHddMMyyyy");
                            prescriptionId = sdf.format(new Date());
                            prescriptionUrl = uri.toString();
                            Map<String, Object> uploadNewPrescription = new HashMap<>();
                            uploadNewPrescription.put("prescriptionId", prescriptionId);
                            uploadNewPrescription.put("productId", productId);
                            uploadNewPrescription.put("sellerId", sellerId);
                            uploadNewPrescription.put("userId", currentUserUid);
                            uploadNewPrescription.put("productName", name);
                            uploadNewPrescription.put("imageUrl", uri.toString());
                            mDb.collection("users").document(currentUserUid).collection("prescriptions")
                                    .document(prescriptionId).set(uploadNewPrescription);
                            progressDialog.dismiss();

                            addProductToCart();
                        }
                    });
                }
            });
        }
        if (filePath == null) {
            Toast.makeText(this, "Please Upload Prescription", Toast.LENGTH_LONG).show();
        }
    }

    private void addProductToCart() {
        SimpleDateFormat sdf = new SimpleDateFormat("ssmmHHddMMyyyy");
        final String productOrderId = sdf.format(new Date());
        Map<String, Object> order = new HashMap<>();
        order.put("name", name);
        order.put("mrp", mrp);
        order.put("price", price);
        order.put("discount", discount);
        order.put("description", description);
        order.put("category", category);
        order.put("imageUrl", imageUrl);
        order.put("userId", currentUserUid);
        order.put("sellerId", sellerId);
        order.put("sellerToken", sellerToken);
        order.put("productId", productId);
        order.put("productOrderId", productOrderId);
        order.put("seller", seller);
        order.put("rating", rating);
        order.put("review", review);
        order.put("prescriptionUrl", prescriptionUrl);
        order.put("prescriptionId", prescriptionId);
        order.put("productQuantity", String.valueOf(productQuantity));
        order.put("status", "in cart");
        mDb.collection("users").document(currentUserUid)
                .collection("orders").document(productOrderId)
                .set(order);
        Toast.makeText(productDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), cart.class));
        finish();
    }
}