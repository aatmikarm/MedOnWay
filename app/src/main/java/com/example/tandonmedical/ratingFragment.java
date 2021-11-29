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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ratingFragment extends Fragment {

    private ConstraintLayout rating_frag_full_cl;
    private String productId, currentUserUid;
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

        rating_frag_full_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), rating.class));
            }
        });

    }

}