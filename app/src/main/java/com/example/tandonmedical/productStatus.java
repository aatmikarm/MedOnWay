package com.example.tandonmedical;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class productStatus extends AppCompatActivity {

    private String currentUserUid;

    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    private String seller, sellerId, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_status);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        if (getIntent().getExtras() != null) {

            this.productId = (String) getIntent().getExtras().get("productId");
            this.sellerId = (String) getIntent().getExtras().get("sellerId");
            this.seller = (String) getIntent().getExtras().get("seller");

        }

        getSellerDetails();


    }

    private void getSellerDetails() {

        mDb.collection("seller")
                .document(sellerId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        seller = document.get("name").toString();



                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}