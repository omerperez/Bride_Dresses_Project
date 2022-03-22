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


public class DressDescriptionFragment extends Fragment {

    public static final String DRESS_PARAM = "Dress";
    MaterialButton backButton;
    MaterialButton editButton;
    MaterialButton deleteButton;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dress_description, container, false);
        Dress dress = DressDescriptionFragmentArgs.fromBundle(getArguments()).getDress();
        TextView dressType = view.findViewById(R.id.dressItemType);
        TextView dressDesigner = view.findViewById(R.id.dressItemDesigner);
        TextView dressPrice = view.findViewById(R.id.dressItemPrice);
        ImageView dressImage = view.findViewById(R.id.dressItemImage);
        editButton = view.findViewById(R.id.dress_description_page_edit_button);
        deleteButton = view.findViewById(R.id.dress_description_page_delete_button);
        backButton = view.findViewById(R.id.dress_description_back_button);
        navController = NavHostFragment.findNavController(this);
        dressType.setText(dress.getType());
        dressPrice.setText(dress.getPrice());
        // dressDesigner.setText(mDress.getUser().getFullName()); TODO
        Picasso.get().load(dress.getImageUrl()).into(dressImage);
        setDeleteButtonOnClickListener();
//        setEditButtonOnClickListener();
        return view;
    }


    private void setDeleteButtonOnClickListener() {
        deleteButton.setOnClickListener(view -> {
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            new Thread(() -> {
                Dress dress = DressDescriptionFragmentArgs.fromBundle(getArguments()).getDress();
                AppLocalDb.db.dressDao().delete(dress);
                Model.instance.deleteDress(dress, () -> navController.navigateUp());
            }).start();
        });
    }
//    private void setEditButtonOnClickListener() {
//        editButton.setOnClickListener(view -> {
//            navController.navigate(DressDescriptionFragmentDirections.actionDressDescriptionFragmentToCreateDressFragment());
//        });
//    }
}