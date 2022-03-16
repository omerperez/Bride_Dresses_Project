package com.example.bride_dresses_project.fragments.dresses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Dress;
import com.google.gson.Gson;


public class DressDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String DRESS_PARAM = "Dress";
    // TODO: Rename and change types of parameters
    private Dress mDress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String stringObjectDress = getArguments().getString(DRESS_PARAM);
            Gson g = new Gson();
            mDress = g.fromJson(stringObjectDress,Dress.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dress_description, container, false);
    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        if(mDress!=null) {
//            TextView tv = view.findViewById(R.id.dressItemType);
//            tv.setText(mDress.getType());
//        }
//    }
}