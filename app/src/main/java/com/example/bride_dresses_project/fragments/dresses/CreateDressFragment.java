package com.example.bride_dresses_project.fragments.dresses;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.databinding.FragmentHomeBinding;
import com.example.bride_dresses_project.model.Dress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

public class CreateDressFragment extends Fragment {
    EditText price, type;
    ImageView dressImage;
    Button saveBtn, uploadImageBtn;
    String fileName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_create_dress, container, false);
        price=view.findViewById(R.id.create_dress_price);
        type=view.findViewById(R.id.create_dress_type);
        dressImage=view.findViewById(R.id.create_dress_image);
        uploadImageBtn = view.findViewById(R.id.create_dress_upload_btn);
        saveBtn=view.findViewById(R.id.create_dress_save_btn);

//        dressImage.setOnClickListener(new View.OnClickListener() {
//                                          @Override
//                                          public void onClick(View v) {
//                                              takeImage();
//                                          }
//
//                                      });

//        saveBtn.setOnClickListener((v)->{
////            Navigation.findNavController(v).navigate(FragmentHomeBinding.actionNewUserFragmentToHomeFragment());
//        });

        uploadImageBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_createDressFragment_to_googleMapsFragment);
        });
                saveBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        final String priceTxt = price.getText().toString();
                        final String typeTxt = type.getText().toString();
//                Dress dress = new Dress(priceTxt,typeTxt,fileName);
                        Dress dress = new Dress(priceTxt, typeTxt, "Linoy Bekker");

                        if (priceTxt.isEmpty() || typeTxt.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        } else if (priceTxt.indexOf("-") != -1) {
                            Toast.makeText(getContext(), "Price is Illegal", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("dresses").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(dress.getId())) {
                                        Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        databaseReference.child("dresses").child(dress.getId()).setValue(dress);
                                        Toast.makeText(getContext(), "Dress Created successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        }
                    }
                });

        return view;
    }


}