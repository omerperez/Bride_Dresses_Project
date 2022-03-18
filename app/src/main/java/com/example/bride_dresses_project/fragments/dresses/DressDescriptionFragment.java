package com.example.bride_dresses_project.fragments.dresses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Dress;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


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
        View view= inflater.inflate(R.layout.fragment_dress_description, container, false);
        TextView dressType=view.findViewById(R.id.dressItemType);
        TextView dressDesigner=view.findViewById(R.id.dressItemDesigner);
        TextView dressPrice=view.findViewById(R.id.dressItemPrice);
        ImageView dressImage=view.findViewById(R.id.dressItemImage);
        MaterialButton editButton=view.findViewById(R.id.dress_description_edit_button);
        MaterialButton deleteButton=view.findViewById(R.id.dress_description_delete_button);
        MaterialButton backButton=view.findViewById(R.id.dress_description_back_button);

        dressType.setText(mDress.getType());
        dressPrice.setText(mDress.getPrice());
       // dressDesigner.setText(mDress.getUser().getFullName());
        Picasso.get().load(mDress.getImageUrl()).into(dressImage);

        backButton.setOnClickListener(view1 -> {
            Navigation.findNavController(getView()).navigate(R.id.action_dressDescriptionFragment_to_dressesListFragment);

        });
        return  view;
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