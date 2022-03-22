package com.example.bride_dresses_project.fragments.dresses;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.button.MaterialButton;

import java.util.UUID;

public class AddDressFragment extends Fragment {
    private static final int REQUEST_CAMERA =1 ;
    EditText price, type;
    ImageButton camBtn;
    Button saveBtn, uploadImageBtn;
    Uri dressImageUri;
    Model model = Model.instance;
    ProgressBar progressBar;
    Bitmap imageBitmap;
    ImageButton galleryBtn;
    ImageView image;
    MaterialButton backButton;
    String ownerId;
    public static int PICK_IMAGE_REQUEST = 2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_dress,container, false);
        ownerId = Model.instance.getOwnerId();
        Log.d("owner1", ownerId);
        Log.d("owner2", Model.instance.getOwnerId());
        type = view.findViewById(R.id.add_dress_type);
        price = view.findViewById(R.id.add_dress_price);
        saveBtn = view.findViewById(R.id.add_dress_save_btn);
        progressBar = view.findViewById(R.id.main_progressbar);
        progressBar.setVisibility(View.GONE);
        camBtn = view.findViewById(R.id.main_cam_btn);
        image = view.findViewById(R.id.image);
        backButton=view.findViewById(R.id.add_dress_back_button);
        backButton.setOnClickListener(view1 -> {
            Navigation.findNavController(getView()).navigate(R.id.action_createDressFragment_to_dressesListFragment);
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        galleryBtn = view.findViewById(R.id.main_gallery_btn);

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });
        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void openCam() {
        if(getContext()==null || getActivity() ==null)return;
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},5);
        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA){
            if (resultCode == Activity.RESULT_OK) {
                if (data != null & getActivity() != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    image.setImageBitmap(photo);
                    try {
                        this.imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
        }else if(requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK){

                if(data!=null & getActivity()!=null) {
                   Uri imageUri = data.getData();
                    //camBtn.setImageURI(imageUri);
                    image.setImageURI(imageUri);

                    try {
                        this.imageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        String dressType = type.getText().toString();
        String dressPrice = price.getText().toString();
        Dress dress = new Dress(dressType,dressPrice,"", ownerId);
        Log.d("owner2", ownerId);

        if (imageBitmap == null){
            Model.instance.addDress(dress,()->{
                Navigation.findNavController(type).navigateUp();
            });
        }else{
            Model.instance.saveImage(imageBitmap, UUID.randomUUID() + ".jpg", url -> {
                dress.setImageUrl(url);
                Model.instance.addDress(dress,()->{
                    Navigation.findNavController(type).navigateUp();
                });
            });
        }
    }




}