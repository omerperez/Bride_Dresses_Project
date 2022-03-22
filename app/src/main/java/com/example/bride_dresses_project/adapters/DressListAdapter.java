package com.example.bride_dresses_project.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.entities.Dress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class DressListAdapter extends RecyclerView.Adapter<DressListAdapter.DressViewHolder> {
    private final OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(Dress dress);
    }

    private final Activity mActivity;

    public DressListAdapter(Activity activity, OnItemClickListener listener) {
        mActivity = activity;
        mListener = listener;
    }

    private final DiffUtil.ItemCallback differCallback = new DiffUtil.ItemCallback<Dress>() {
        @Override
        public boolean areItemsTheSame(@NonNull Dress oldItem, @NonNull Dress newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Dress oldItem, @NonNull Dress newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    public AsyncListDiffer<Dress> differ = new AsyncListDiffer<Dress>(this, differCallback);


    @NonNull
    @Override
    public DressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.dress_item, parent, false);
        DressViewHolder holder = new DressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DressViewHolder holder, int position) {
        holder.bind(differ.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class DressViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView price, type;
        FloatingActionButton addBtn;
        RecyclerView rvDresses;
        Button dressItemMoreDetails;

        public DressViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.dressItemType);
            price = itemView.findViewById(R.id.dressItemPrice);
            image = itemView.findViewById(R.id.dressItemImage);
            dressItemMoreDetails = itemView.findViewById(R.id.dressItemMoreDetails);
            dressItemMoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(differ.getCurrentList().get(getAdapterPosition()));
                }
            });
        }

        void bind(Dress dress) {
            type.setText(dress.getType());
            price.setText(dress.getPrice());
            image.setImageResource(R.drawable.dress_icon);
            if (dress.getImageUrl() != null && !dress.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(dress.getImageUrl())
                        .into(image);
            }
        }
    }

}