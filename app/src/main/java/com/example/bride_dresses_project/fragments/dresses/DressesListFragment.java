package com.example.bride_dresses_project.fragments.dresses;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class DressesListFragment extends Fragment {

    DressesListRvViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    FloatingActionButton addDressButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(DressesListRvViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dresses_list, container, false);
        addDressButton = view.findViewById(R.id.add_new_dress_btn);
        addDressButton.setOnClickListener((view1 -> Navigation.findNavController(view).navigate(R.id.action_dressesListFragment_to_createDressFragment)));
        swipeRefresh = view.findViewById(R.id.dresseslist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshDressList());
        RecyclerView list = view.findViewById(R.id.dressRv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Dress dress = viewModel.getData().getValue().get(position);
                Gson g = new Gson();
                String stringFromObject = g.toJson(dress);
                Bundle b = new Bundle();
                b.putString(DressDescriptionFragment.DRESS_PARAM,stringFromObject);
                Navigation.findNavController(v).navigate(R.id.action_dressesListFragment_to_dressDescriptionFragment,b);
            }
        });
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getDressListLoadingState().getValue() == Model.DressListLoadingState.loading);
        Model.instance.getDressListLoadingState().observe(getViewLifecycleOwner(), dressListLoadingState -> {
            if (dressListLoadingState == Model.DressListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;
    }


    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView price, type;
        FloatingActionButton addBtn;
        RecyclerView rvDresses;
        Button dressItemMoreDetails;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            type = itemView.findViewById(R.id.dressItemType);
            price = itemView.findViewById(R.id.dressItemPrice);
            image = itemView.findViewById(R.id.dressItemImage);
            dressItemMoreDetails= itemView.findViewById(R.id.dressItemMoreDetails);
            dressItemMoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
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

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class
    MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.dress_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Dress dress = viewModel.getData().getValue().get(position);
            holder.bind(dress);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) {
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
    }
}
