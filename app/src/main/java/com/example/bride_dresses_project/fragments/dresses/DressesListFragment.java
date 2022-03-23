package com.example.bride_dresses_project.fragments.dresses;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.adapters.DressListAdapter;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;


public class DressesListFragment extends Fragment implements DressListAdapter.OnItemClickListener {

    private DressesListRvViewModel viewModel;
    private DressListAdapter mAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(DressesListRvViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dresses_list, container, false);
        swipeRefresh = view.findViewById(R.id.dresseslist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshDressList(false));
        RecyclerView recyclerView = view.findViewById(R.id.dressRv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DressListAdapter(getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        viewModel.getAllDresses().observe(getViewLifecycleOwner(), dresses -> mAdapter.differ.submitList(dresses));
        swipeRefresh.setRefreshing(Model.instance.getDressListLoadingState().getValue() == Model.DressListLoadingState.loading);
        Model.instance.getDressListLoadingState().observe(getViewLifecycleOwner(), dressListLoadingState -> swipeRefresh.setRefreshing(dressListLoadingState == Model.DressListLoadingState.loading));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshDressList(false);
    }

    @Override
    public void onItemClick(Dress dress) {
        NavHostFragment.findNavController(this).navigate(DressesListFragmentDirections.actionDressesListFragmentToDressDescriptionFragment(dress));
    }
}
