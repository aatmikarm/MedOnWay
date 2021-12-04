package com.example.tandonmedical;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ratingFragment extends Fragment {

    private ConstraintLayout rating_frag_full_cl;
    private String productId, currentUserUid, rating, review;
    private int numberOfRatings, fiveStar, fourStar, threeStar, twoStar, oneStar;
    private RatingBar rating_frag_rb;
    private TextView rating_frag_tv, rating_frag_review_tv, review_frag_5_tv,
            review_frag_4_tv, review_frag_3_tv, review_frag_2_tv, review_frag_1_tv;
    private ProgressBar rating_frag_5_pb, rating_frag_4_pb, rating_frag_3_pb, rating_frag_2_pb, rating_frag_1_pb;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    public ratingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rating, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.productId = arguments.get("productId").toString();
        }
        rating_frag_full_cl = (ConstraintLayout) view.findViewById(R.id.rating_frag_full_cl);
        rating_frag_rb = (RatingBar) view.findViewById(R.id.rating_frag_rb);
        rating_frag_tv = (TextView) view.findViewById(R.id.rating_frag_tv);
        review_frag_5_tv = (TextView) view.findViewById(R.id.review_frag_5_tv);
        review_frag_4_tv = (TextView) view.findViewById(R.id.review_frag_4_tv);
        review_frag_3_tv = (TextView) view.findViewById(R.id.review_frag_3_tv);
        review_frag_2_tv = (TextView) view.findViewById(R.id.review_frag_2_tv);
        review_frag_1_tv = (TextView) view.findViewById(R.id.review_frag_1_tv);
        rating_frag_review_tv = (TextView) view.findViewById(R.id.rating_frag_review_tv);
        rating_frag_5_pb = (ProgressBar) view.findViewById(R.id.rating_frag_5_pb);
        rating_frag_4_pb = (ProgressBar) view.findViewById(R.id.rating_frag_4_pb);
        rating_frag_3_pb = (ProgressBar) view.findViewById(R.id.rating_frag_3_pb);
        rating_frag_2_pb = (ProgressBar) view.findViewById(R.id.rating_frag_2_pb);
        rating_frag_1_pb = (ProgressBar) view.findViewById(R.id.rating_frag_1_pb);

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        getRatingAndReview();
        rating_frag_full_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), rating.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("productId", productId);
                v.getContext().startActivity(intent);
            }
        });

    }

    private void getRatingAndReview() {
        numberOfRatings = 0;
        fiveStar = 0;
        fourStar = 0;
        threeStar = 0;
        twoStar = 0;
        oneStar = 0;
        mDb.collection("products").document(productId).collection("rating")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        float productRating = Float.parseFloat((String) document.get("rating"));
                        if (productRating <= 1 && productRating >= 0) {
                            oneStar = oneStar + 1;
                        }
                        if (productRating <= 2 && productRating >= 1) {
                            twoStar = twoStar + 1;
                        }
                        if (productRating <= 3 && productRating >= 2) {
                            threeStar = threeStar + 1;
                        }
                        if (productRating <= 4 && productRating >= 3) {
                            fourStar = fourStar + 1;
                        }
                        if (productRating <= 5 && productRating >= 4) {
                            fiveStar = fiveStar + 1;
                        }
                        numberOfRatings = numberOfRatings + 1;

                    }
                    rating_frag_review_tv.setText(numberOfRatings + " Ratings and " + numberOfRatings + " Reviews");
                    review_frag_1_tv.setText(String.valueOf(oneStar));
                    review_frag_2_tv.setText(String.valueOf(twoStar));
                    review_frag_3_tv.setText(String.valueOf(threeStar));
                    review_frag_4_tv.setText(String.valueOf(fourStar));
                    review_frag_5_tv.setText(String.valueOf(fiveStar));

                    if (numberOfRatings != 0) {
                        rating_frag_1_pb.setProgress((oneStar * 100) / numberOfRatings);
                        rating_frag_2_pb.setProgress((twoStar * 100) / numberOfRatings);
                        rating_frag_3_pb.setProgress((threeStar * 100) / numberOfRatings);
                        rating_frag_4_pb.setProgress((fourStar * 100) / numberOfRatings);
                        rating_frag_5_pb.setProgress((fiveStar * 100) / numberOfRatings);

                        //calculate average rating
                        float avgRating = ((5 * fiveStar) + (4 * fourStar) + (3 * threeStar) + (2 * twoStar) + (1 * oneStar)) / numberOfRatings;
                        rating_frag_tv.setText(String.valueOf(avgRating));
                        rating_frag_rb.setRating(avgRating);

                        String[] star = {
                                String.valueOf(oneStar),
                                String.valueOf(twoStar),
                                String.valueOf(threeStar),
                                String.valueOf(fourStar),
                                String.valueOf(fiveStar)
                        };
                        List<String> stars = Arrays.asList(star);
                        Map<String, Object> updateIndividualRating = new HashMap<>();
                        updateIndividualRating.put("rating", String.valueOf(numberOfRatings));
                        updateIndividualRating.put("review", String.valueOf(numberOfRatings));
                        updateIndividualRating.put("avgRating", String.valueOf(avgRating));
                        updateIndividualRating.put("stars", stars);
                        mDb.collection("products").document(productId).update(updateIndividualRating);
                    }
                }
            }
        });
    }
}