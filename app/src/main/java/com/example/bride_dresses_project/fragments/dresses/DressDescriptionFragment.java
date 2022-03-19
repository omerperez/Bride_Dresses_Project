package com.example.bride_dresses_project.fragments.dresses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Dress;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class DressDescriptionFragment extends Fragment {


    public static final String DRESS_PARAM = "Dress";
    private Dress mDress;
    MaterialButton backButton;
    MaterialButton editButton;
    MaterialButton deleteButton;
    private NavController navController;


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
      editButton=view.findViewById(R.id.dress_description_edit_button);
         deleteButton=view.findViewById(R.id.dress_description_delete_button);
         backButton=view.findViewById(R.id.dress_description_back_button);
        navController = NavHostFragment.findNavController(this);

        dressType.setText(mDress.getType());
        dressPrice.setText(mDress.getPrice());
       // dressDesigner.setText(mDress.getUser().getFullName());
        Picasso.get().load(mDress.getImageUrl()).into(dressImage);
        setBackButtonOnClickListener();
        setDeleteButtonOnClickListener();


        return  view;
    }

        private void setBackButtonOnClickListener() {
            backButton.setOnClickListener(view1 -> {
                Navigation.findNavController(getView()).navigate(R.id.action_dressDescriptionFragment_to_dressesListFragment);
            });
        }

    private void setDeleteButtonOnClickListener() {
        deleteButton.setOnClickListener(view -> {
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            Model.instance.deleteDress(mDress, () -> {
                navController.navigateUp();
            });
        });
    }

//    private void setEditButtonOnClickListener() {
//        editButton.setOnClickListener(view -> {
//            navController.navigate(DressDescriptionFragment.DRE   .actionReviewDetailsFragmentToAddReviewFragment(true));
//        });
//    }
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