package com.example.bride_dresses_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePageFragment extends Fragment {

    Button addBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        addBtn = view.findViewById(R.id.homepage_add_dress);
        addBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_homePageFragment_to_createDressFragment);
        });
        return view;
    }
}