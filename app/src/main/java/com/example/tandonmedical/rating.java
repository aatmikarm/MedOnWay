package com.example.tandonmedical;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class rating extends AppCompatActivity {

    private EditText new_rating_review_et;
    private RatingBar new_rating_rb;
    private CardView rating_and_review_upload_cv;
    private ImageView rating_back_iv, new_rating_review_upload_product_iv;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid, productId, rating, review, imageUrl, name;
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
        new_rating_review_et = findViewById(R.id.new_rating_review_et);
        rating_and_review_rv = findViewById(R.id.rating_and_review_rv);
        rating_and_review_upload_cv = findViewById(R.id.rating_and_review_upload_cv);


        if (getIntent().getExtras() != null) {
            this.productId = (String) getIntent().getExtras().get("productId");
        }
        rating_and_review_upload_cv.setVisibility(View.GONE);
        rating_and_review_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        ratingReviewModelLists = new ArrayList<>();
        ratingReviewModelLists = getReviewsAndRating();
        getCurrentUserDetails();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rating_and_review_upload_cv.setVisibility(View.VISIBLE);
                ratingReviewAdapter = new ratingReviewAdapter(getApplicationContext(), ratingReviewModelLists);
                rating_and_review_rv.setAdapter(ratingReviewAdapter);
            }
        }, 3000);

        rating_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rating_and_review_upload_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadRatingAndReview();
            }
        });

    }

    private void uploadRatingAndReview() {
        SimpleDateFormat sdf = new SimpleDateFormat("ssmmHHddMMyyyy");
        final String time = sdf.format(new Date());
        Map<String, Object> uploadRatingAndReview = new HashMap<>();
        uploadRatingAndReview.put("productId", productId);
        uploadRatingAndReview.put("userId", currentUserUid);
        uploadRatingAndReview.put("time", time);
        uploadRatingAndReview.put("rating", String.valueOf(new_rating_rb.getRating()));
        uploadRatingAndReview.put("review", new_rating_review_et.getText().toString());
        uploadRatingAndReview.put("imageUrl", imageUrl);
        uploadRatingAndReview.put("name", name);
        mDb.collection("products").document(productId)
                .collection("rating").document().set(uploadRatingAndReview);


        ratingReviewModelList ratingReviewModelList = new ratingReviewModelList();
        ratingReviewModelList.setName(name);
        ratingReviewModelList.setImageUrl(imageUrl);
        ratingReviewModelList.setProductId(productId);
        ratingReviewModelList.setRating(String.valueOf(new_rating_rb.getRating()));
        ratingReviewModelList.setReview(review);
        ratingReviewModelList.setTime(String.valueOf(time));
        ratingReviewModelList.setUserId(currentUserUid);
        ratingReviewModelLists.add(ratingReviewModelList);
        ratingReviewAdapter.notifyDataSetChanged();
    }

    private void getCurrentUserDetails() {
        mDb.collection("users").document(currentUserUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    imageUrl = (String) document.get("imageUrl");
                    name = (String) document.get("name");
                }
            }
        });
    }


    private ArrayList<ratingReviewModelList> getReviewsAndRating() {
        final ArrayList<ratingReviewModelList> ratingReviewModelLists = new ArrayList<>();
        mDb.collection("products").document(productId).collection("rating")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ratingReviewModelList ratingReviewModelList = new ratingReviewModelList();
                        ratingReviewModelList.setName((String) document.get("name"));
                        ratingReviewModelList.setImageUrl((String) document.get("imageUrl"));
                        ratingReviewModelList.setProductId((String) document.get("productId"));
                        ratingReviewModelList.setRating((String) document.get("rating"));
                        ratingReviewModelList.setReview((String) document.get("review"));
                        ratingReviewModelList.setTime((String) document.get("time"));
                        ratingReviewModelList.setUserId((String) document.get("userId"));
                        ratingReviewModelLists.add(ratingReviewModelList);
                    }
                }
            }
        });
        return ratingReviewModelLists;
    }
}