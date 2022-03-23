package com.example.bride_dresses_project.fragments.dresses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.AppLocalDb;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class DressDescriptionFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dress_description, container, false);
        Dress dress = DressDescriptionFragmentArgs.fromBundle(getArguments()).getDress();
        TextView dressType = view.findViewById(R.id.dressItemType);
        TextView dressDesigner = view.findViewById(R.id.dressItemDesigner);
        TextView dressPrice = view.findViewById(R.id.dressItemPrice);
        ImageView dressImage = view.findViewById(R.id.dressItemImage);
        view.findViewById(R.id.dress_description_page_edit_button).setOnClickListener(this);
        view.findViewById(R.id.dress_description_page_delete_button).setOnClickListener(this);
        view.findViewById(R.id.dress_description_back_button).setOnClickListener(this);
        dressType.setText(dress.getType());
        dressPrice.setText(dress.getPrice());
        Picasso.get().load(dress.getImageUrl()).into(dressImage);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dress_description_page_edit_button: {
                Dress dress = DressDescriptionFragmentArgs.fromBundle(getArguments()).getDress();
                NavHostFragment.findNavController(this).navigate(DressDescriptionFragmentDirections.actionDressDescriptionFragmentToCreateDressFragment3(dress));
                break;
            }
            case R.id.dress_description_page_delete_button: {
                new Thread(() -> {
                    Dress dress = DressDescriptionFragmentArgs.fromBundle(getArguments()).getDress();
                    AppLocalDb.db.dressDao().delete(dress);
                    Model.instance.deleteDress(dress, () -> navigateUp());
                }).start();
                break;
            }
            case R.id.dress_description_back_button: {
                navigateUp();
                break;
            }
        }
    }

    private void navigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }
}