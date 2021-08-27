package com.example.tandonmedical;

import android.content.Context;
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
