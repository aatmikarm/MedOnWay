package com.example.tandonmedical;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<categoriesModelList> categoriesModelLists;

    public categoriesAdapter(Context context, ArrayList<categoriesModelList> categoriesModelLists) {

        this.context = context;
        this.categoriesModelLists = categoriesModelLists;

    }

    @Override
    public categoriesAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_recycler_view, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(categoriesAdapter.ItemViewHolder holder, int position) {

        Glide.with(context).load(categoriesModelLists.get(position).getImageUrl()).into(holder.categoriesImage);
        holder.categoriesName.setText(categoriesModelLists.get(position).name);
        holder.categoriesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, specificCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category", categoriesModelLists.get(position).getName());
                intent.putExtra("imageUrl", categoriesModelLists.get(position).getImageUrl());
                Toast.makeText(context, categoriesModelLists.get(position).getName(), Toast.LENGTH_LONG).show();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelLists.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView categoriesImage;
        TextView categoriesName;

        ItemViewHolder(View itemView) {
            super(itemView);

            categoriesName = itemView.findViewById(R.id.rv_categories_name);
            categoriesImage = itemView.findViewById(R.id.rv_categories_image);


        }
    }
}
