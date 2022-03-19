package com.example.bride_dresses_project.fragments.dresses;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.bride_dresses_project.DressItemClickListener;
import com.example.bride_dresses_project.adapters.DressesRvAdapter;
import com.example.bride_dresses_project.Interfaces.DressItemClickListener;
import com.example.bride_dresses_project.DressesRvAdapter;
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.entities.Dress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

public class DressesListFragment extends Fragment  implements DressItemClickListener {


    FloatingActionButton addBtn;
    RecyclerView rvDresses;
    DressListViewModel viewModel;

    public DressesListFragment(){}

    boolean isLoading = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dresses_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBtn = view.findViewById(R.id.add_new_dress_btn);
        addBtn.setOnClickListener((v)-> Navigation.findNavController(v).navigate(R.id.action_dressesListFragment_to_createDressFragment));
        rvDresses = view.findViewById(R.id.dressRv);

        rvDresses.setLayoutManager(new LinearLayoutManager(getContext()));
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Bride Dresses");
        dialog.setMessage("Loading..");
        dialog.show();
        isLoading = true;
        viewModel = new ViewModelProvider(this).get(DressListViewModel.class);


        viewModel.getDressesLiveData().observe(getViewLifecycleOwner(), dresses -> {
            DressesRvAdapter adapter = new DressesRvAdapter(dresses,DressesListFragment.this);
            rvDresses.setAdapter(adapter);
            dialog.dismiss();
            isLoading = false;
        });

        viewModel.getExceptionsLiveData().observe(getViewLifecycleOwner(), e -> {
            Toast.makeText(getContext(),"There was a problem loading the dresses",Toast.LENGTH_SHORT).show();
            Log.d("DressListFragment","getAllDresses " + e.getMessage());
            dialog.dismiss();
            isLoading = false;
        });
    }


    @Override
    public void onClick(Dress dress) {
        if(getView() == null)  return;
        Gson g = new Gson();
        String stringFromObject = g.toJson(dress);
        Bundle b = new Bundle();
        b.putString(DressDescriptionFragment.DRESS_PARAM,stringFromObject);
        Navigation.findNavController(getView()).navigate(R.id.action_dressesListFragment_to_dressDescriptionFragment,b);
    }
}