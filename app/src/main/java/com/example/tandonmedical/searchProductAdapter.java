package com.example.tandonmedical;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class searchProductAdapter extends RecyclerView.Adapter<searchProductAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<productModelList> productModelList;
    private searchProductInterface searchProductInterface;

    public searchProductAdapter(Context context, ArrayList<productModelList> productModelList, com.example.tandonmedical.searchProductInterface searchProductInterface) {

        this.context = context;
        this.productModelList = productModelList;
        this.searchProductInterface = searchProductInterface;

    }


    @Override
    public searchProductAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_rv_product, parent, false);
        return new searchProductAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(searchProductAdapter.ItemViewHolder holder, int position) {

        Glide.with(context).load(productModelList.get(position).getImageUrl()).into(holder.productImage);
        holder.productName.setText(productModelList.get(position).name);

        holder.search_star_tv.setText(productModelList.get(position).rating);
        holder.search_star_rb.setRating(4);
        holder.productPrice.setText("Rs. " + productModelList.get(position).price + ".00");
        holder.productMrp.setText("Rs. " + productModelList.get(position).mrp + ".00");
        holder.productDiscount.setText(productModelList.get(position).discount + "% OFF");
        holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName,search_star_tv, productPrice, productMrp, productDiscount;
        ConstraintLayout productContainer;
        RatingBar search_star_rb;

        ItemViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.search_name);
            productImage = itemView.findViewById(R.id.search_rv_user_iv);
            productPrice = itemView.findViewById(R.id.search_product_discounted_price);
            productMrp = itemView.findViewById(R.id.search_mrp_tv);
            productContainer = itemView.findViewById(R.id.search_product_container);
            productDiscount = itemView.findViewById(R.id.search_discount_text_view);
            search_star_tv = itemView.findViewById(R.id.search_star_tv);
            search_star_rb = itemView.findViewById(R.id.search_star_rb);

            productContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchProductInterface.searchProductOnClickInterface(getAdapterPosition());
                }
            });


        }
    }

    public void filterList(ArrayList<productModelList> filteredList) {
        productModelList = filteredList;
        notifyDataSetChanged();
    }

}
