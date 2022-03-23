package com.example.bride_dresses_project.fragments.dresses;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.adapters.DressListAdapter;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.Dress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyProfileFragment extends Fragment implements DressListAdapter.OnItemClickListener {

    private DressesListRvViewModel viewModel;
    private DressListAdapter mAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private View myProfileView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(DressesListRvViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myProfileView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        swipeRefresh = myProfileView.findViewById(R.id.profile_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshDressList(true));
        RecyclerView recyclerView = myProfileView.findViewById(R.id.dressRv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DressListAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        viewModel.getAllDresses().observe(getViewLifecycleOwner(), dresses -> mAdapter.differ.submitList(dresses));
        swipeRefresh.setRefreshing(Model.instance.getDressListLoadingState().getValue() == Model.DressListLoadingState.loading);
        Model.instance.getDressListLoadingState().observe(getViewLifecycleOwner(), dressListLoadingState -> swipeRefresh.setRefreshing(dressListLoadingState == Model.DressListLoadingState.loading));

        return myProfileView;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshDressList(true);
    }

    @Override
    public void onItemClick(Dress dress) {
        NavHostFragment.findNavController(this).navigate(DressesListFragmentDirections.actionDressesListFragmentToDressDescriptionFragment(dress));
    }

}