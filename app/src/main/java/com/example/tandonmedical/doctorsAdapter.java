package com.example.tandonmedical;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class doctorsAdapter extends RecyclerView.Adapter<doctorsAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<doctorsModelList> doctorsModelLists;

    public doctorsAdapter(Context context , ArrayList<doctorsModelList> doctorsModelLists){

        this.context = context;
        this.doctorsModelLists = doctorsModelLists;

    }

    @Override
    public doctorsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_recycler_view,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(doctorsAdapter.ItemViewHolder holder, int position) {

        Glide.with(context).load(doctorsModelLists.get(position).getImageUrl()).into(holder.doctorImage);
        holder.doctorName.setText(doctorsModelLists.get(position).name);
        holder.doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "url = "+proRecyclerViewListModels.get(position).getImageUrl(), Toast.LENGTH_LONG).show();
                //add(position , proRecyclerViewListModels.get(1));
                //remove(position);
                Intent intent = new Intent(context, doctorDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", doctorsModelLists.get(position).getName());
                intent.putExtra("email", doctorsModelLists.get(position).getEmail());
                intent.putExtra("password", doctorsModelLists.get(position).getPassword());
                intent.putExtra("bio", doctorsModelLists.get(position).getBio());
                intent.putExtra("imageUrl", doctorsModelLists.get(position).getImageUrl());
                intent.putExtra("category", doctorsModelLists.get(position).getCategory());
                intent.putExtra("address", doctorsModelLists.get(position).getAddress());
                intent.putExtra("doctorId", doctorsModelLists.get(position).getDoctorId());
                intent.putExtra("phone", doctorsModelLists.get(position).getPhone());
                intent.putExtra("experience", doctorsModelLists.get(position).getExperience());
                intent.putExtra("status", doctorsModelLists.get(position).getStatus());
                intent.putExtra("rating", doctorsModelLists.get(position).getRating());
                intent.putExtra("review", doctorsModelLists.get(position).getReview());
                intent.putExtra("doctorToken", doctorsModelLists.get(position).getDoctorToken());
                intent.putExtra("education", doctorsModelLists.get(position).getEducation());
                intent.putExtra("hospital", doctorsModelLists.get(position).getHospital());
                intent.putExtra("degree", doctorsModelLists.get(position).getDegree());
                intent.putExtra("timing", doctorsModelLists.get(position).getTiming());
                intent.putExtra("fee", doctorsModelLists.get(position).getFee());
                intent.putExtra("tag", doctorsModelLists.get(position).getTag());
                intent.putExtra("sign", doctorsModelLists.get(position).getSign());
                //intent.putExtra("geo_point", doctorsModelLists.get(position).getGeo_point());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsModelLists.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView doctorImage;
        TextView  doctorName;

        ItemViewHolder(View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.rv_doctor_name);
            doctorImage = itemView.findViewById(R.id.rv_doctor_image);


        }
    }
}
