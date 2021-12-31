package com.example.tandonmedical;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class allDoctorsAdapter extends RecyclerView.Adapter<allDoctorsAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<doctorsModelList> doctorsModelLists;

    public allDoctorsAdapter(Context context , ArrayList<doctorsModelList> doctorsModelLists){

        this.context = context;
        this.doctorsModelLists = doctorsModelLists;

    }

    @Override
    public allDoctorsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_doctors_recycler_view,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(allDoctorsAdapter.ItemViewHolder holder, int position) {

        Glide.with(context).load(doctorsModelLists.get(position).getImageUrl()).into(holder.allDoctors_profile_iv);
        holder.allDoctorsRV_name_tv.setText(doctorsModelLists.get(position).name);
        holder.allDoctors_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "url = "+proRecyclerViewListModels.get(position).getImageUrl(), Toast.LENGTH_LONG).show();
                //add(position , proRecyclerViewListModels.get(1));
                //remove(position);
                Intent intent = new Intent(context, doctorDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", doctorsModelLists.get(position).getName());
                intent.putExtra("doctorId", doctorsModelLists.get(position).getDoctorId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsModelLists.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView allDoctors_profile_iv;
        TextView  allDoctorsRV_name_tv,allDoctorsRV_category_tv,allDoctors_totalStars_tv;
        RatingBar allDoctors_star_rb;

        ItemViewHolder(View itemView) {
            super(itemView);

            allDoctorsRV_name_tv = itemView.findViewById(R.id.allDoctorsRV_name_tv);
            allDoctors_profile_iv = itemView.findViewById(R.id.allDoctors_profile_iv);
            allDoctorsRV_category_tv = itemView.findViewById(R.id.allDoctorsRV_category_tv);
            allDoctors_totalStars_tv = itemView.findViewById(R.id.allDoctors_totalStars_tv);
            allDoctors_star_rb = itemView.findViewById(R.id.allDoctors_star_rb);
        }
    }

    public void filterList(ArrayList<doctorsModelList> filteredList) {
        doctorsModelLists = filteredList;
        notifyDataSetChanged();
    }
}
