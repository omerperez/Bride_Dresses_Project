package com.example.bride_dresses_project;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bride_dresses_project.Interfaces.DressItemClickListener;
import com.example.bride_dresses_project.model.Dress;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class DressesRvAdapter extends RecyclerView.Adapter<DressesRvAdapter.DressViewHolder> {

    private List<Dress> dresses;
    private DressItemClickListener listener;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public DressesRvAdapter(List<Dress> dresses, DressItemClickListener listener) {
        this.dresses = dresses.stream().filter( dress -> {
            return !dress.isDeleted();
        }).collect(Collectors.toList());
        this.listener = listener;
    }

    @NonNull
    @Override
    public DressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View dressItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dress_item,parent,false);
       return new DressViewHolder(dressItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DressViewHolder holder, int position) {
        Dress dress = dresses.get(position);
        holder.priceTextView.setText(dress.getPrice());
        holder.typeTextView.setText(dress.getType());
        holder.designerTextView.setText("unset"); //@TODO Add Designer or User to Dress object
        Picasso.get().load(dress.getImageUrl()).into(holder.imageView);
        holder.moreDetails.setOnClickListener(view -> listener.onClick(dress));
    }

    @Override
    public int getItemCount() {
        return dresses.size();
    }

    class DressViewHolder extends RecyclerView.ViewHolder {
        TextView priceTextView,typeTextView,designerTextView;
        ImageView imageView;
        Button moreDetails;
        public DressViewHolder(@NonNull View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.dressItemPrice);
            typeTextView = itemView.findViewById(R.id.dressItemType);
            designerTextView = itemView.findViewById(R.id.dressItemDesigner);
            imageView = itemView.findViewById(R.id.dressItemImage);
            moreDetails =itemView.findViewById(R.id.dressItemMoreDetails);

        }
    }
}
