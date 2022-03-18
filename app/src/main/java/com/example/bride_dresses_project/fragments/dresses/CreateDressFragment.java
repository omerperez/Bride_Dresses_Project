package com.example.bride_dresses_project.fragments.dresses;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bride_dresses_project.R;

import com.example.bride_dresses_project.model.Dress;
import com.example.bride_dresses_project.model.FirebaseDressStatus;
import com.example.bride_dresses_project.model.Model;
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
    Uri dressImageUri;
    Model model = Model.instance;


    public static int PICK_IMAGE_REQUEST = 1;
    ActivityResultLauncher<Intent> dressImagePickerResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getData()!=null)
                        if(result.getData().getData() != null) {
                            dressImage.setImageURI(result.getData().getData());
                            dressImageUri = result.getData().getData();
                        }
                }
            });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_create_dress, container, false);
        price=view.findViewById(R.id.create_dress_price);
        type=view.findViewById(R.id.create_dress_type);
        dressImage=view.findViewById(R.id.create_dress_image);
        uploadImageBtn = view.findViewById(R.id.create_dress_upload_btn);
        uploadImageBtn.setOnClickListener(view12 -> {


            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            dressImagePickerResultLauncher.launch(i);
        });

        saveBtn=view.findViewById(R.id.create_dress_save_btn);
                saveBtn.setOnClickListener(view1 -> {
                    final String priceTxt = price.getText().toString();
                    final String typeTxt = type.getText().toString();
                    Dress dress = new Dress(typeTxt ,priceTxt, "");// ברגע שאוסיף בלייאאוט את השדות להוסיף גם פה בשביל שזה ישמר בפייר ביס

                    if(dressImageUri ==null) {
                        Toast.makeText(getContext(), "Please Select an image for the dress", Toast.LENGTH_SHORT).show();
                    } else if (priceTxt.isEmpty() || typeTxt.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else if (priceTxt.indexOf("-") != -1) {
                        Toast.makeText(getContext(), "Price is Illegal", Toast.LENGTH_SHORT).show();
                    } else {
                        ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setTitle("Bride Dresses");
                        dialog.setMessage("Loading..");
                        dialog.show();
                        model.addDress(dress,dressImageUri, new Model.AddDressListener(){
                            @Override
                            public void onComplete(FirebaseDressStatus status){
                                dialog.dismiss();
                                Toast.makeText(getContext(),"Successfully added new dress",Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).popBackStack();
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(),"There was a problem adding new dress, please try again later",Toast.LENGTH_SHORT).show();
                                Log.d("CreateDressFragment","Error adding new dress " + e.getMessage());
                                dialog.dismiss();
                            }
                        });
                    }
                });

        return view;
    }


}