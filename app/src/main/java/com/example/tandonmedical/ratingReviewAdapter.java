package com.example.tandonmedical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ratingReviewAdapter extends RecyclerView.Adapter<ratingReviewAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<ratingReviewModelList> ratingReviewModelList;

    public ratingReviewAdapter(Context context, ArrayList<ratingReviewModelList> ratingReviewModelList) {

        this.context = context;
        this.ratingReviewModelList = ratingReviewModelList;


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_and_review_rv, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Glide.with(context).load(ratingReviewModelList.get(position).getImageUrl()).into(holder.user_profile_iv);
        holder.rating_and_review_tv.setText(ratingReviewModelList.get(position).review);
        holder.rating_and_review_star_rb.setRating(Float.parseFloat(ratingReviewModelList.get(position).rating));
        holder.rating_and_review_star_tv.setText(ratingReviewModelList.get(position).rating);
        holder.rating_and_review_user_name_tv.setText(ratingReviewModelList.get(position).name);

        try {
            String oldstring = ratingReviewModelList.get(position).time;
            Date date = new SimpleDateFormat("ssmmHHddMMyyyy").parse(oldstring);
            String newstring = new SimpleDateFormat("EEE, d MMM yyyy").format(date);
            holder.rating_and_review_time_tv.setText(newstring);
        } catch (ParseException e) {
            e.printStackTrace();
        }




        holder.user_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Rating", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ratingReviewModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView user_profile_iv;
        TextView rating_and_review_tv, rating_and_review_star_tv, rating_and_review_user_name_tv, rating_and_review_time_tv;
        RatingBar rating_and_review_star_rb;

        ItemViewHolder(View itemView) {
            super(itemView);
            user_profile_iv = itemView.findViewById(R.id.user_profile_iv);
            rating_and_review_tv = itemView.findViewById(R.id.search_name);
            rating_and_review_star_tv = itemView.findViewById(R.id.search_star_tv);
            rating_and_review_user_name_tv = itemView.findViewById(R.id.search_user_name_tv);
            rating_and_review_time_tv = itemView.findViewById(R.id.rating_and_review_time_tv);
            rating_and_review_star_rb = itemView.findViewById(R.id.search_star_rb);
        }
    }
}
