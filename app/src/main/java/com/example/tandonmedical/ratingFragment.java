package com.example.tandonmedical;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ratingFragment extends Fragment {

    private String productName, currentUserUid;
    private RatingBar search_rating_rb;
    private TextView search_rating_tv, rating_review_tv, review_5_tv, review_4_tv, review_3_tv, review_2_tv, review_1_tv;
    private ProgressBar rating_5_pb, rating_4_pb, rating_3_pb, rating_2_pb, rating_1_pb;
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
            this.productName = arguments.get("productName").toString();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();


        return view;
    }
}