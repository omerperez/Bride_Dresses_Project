package com.example.bride_dresses_project.fragments.dresses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.example.bride_dresses_project.model.entities.Dress;
import com.example.bride_dresses_project.model.firebase.FirebaseDressStatus;
import com.example.bride_dresses_project.model.Model;

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