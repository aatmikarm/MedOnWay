package com.example.tandonmedical;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class rating extends AppCompatActivity {

    private EditText search_et;
    private RatingBar new_rating_rb;
    private TextView new_rating_review_tv;
    private CardView rating_and_review_upload_cv;
    private ImageView rating_back_iv, new_rating_review_upload_product_iv;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid;
    private RecyclerView rating_and_review_rv;
    private ratingReviewAdapter ratingReviewAdapter;
    private ArrayList<ratingReviewModelList> ratingReviewModelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        rating_back_iv = findViewById(R.id.rating_back_iv);
        new_rating_review_upload_product_iv = findViewById(R.id.new_rating_review_upload_product_iv);
        new_rating_rb = findViewById(R.id.new_rating_rb);
        new_rating_review_tv = findViewById(R.id.new_rating_review_tv);
        rating_and_review_rv = findViewById(R.id.rating_and_review_rv);
        rating_and_review_upload_cv = findViewById(R.id.rating_and_review_upload_cv);

        rating_and_review_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        ratingReviewModelLists = new ArrayList<>();
        ratingReviewAdapter = new ratingReviewAdapter(getApplicationContext(), ratingReviewModelLists);
        rating_and_review_rv.setAdapter(ratingReviewAdapter);


        rating_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}