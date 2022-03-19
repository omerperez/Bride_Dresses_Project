package com.example.bride_dresses_project.fragments.auth;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bride_dresses_project.utils.CameraUtilFragment;
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.User;
import com.google.android.material.progressindicator.CircularProgressIndicator;


public class RegisterFragment extends CameraUtilFragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    View registerFragment;
    EditText emailEt,fullNameEt, phoneEt, passwordEt, confirmPasswordEt,streetAddressEt, stateEt, countryEt;
    Button registerBtn;
    TextView clickForLogin;
    ImageView avatarImageView;
    NavController navController;
    CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerFragment = inflater.inflate(R.layout.fragment_register, container, false);

        emailEt = registerFragment.findViewById(R.id.register_email_et);
        fullNameEt = registerFragment.findViewById(R.id.register_name_et);
        phoneEt = registerFragment.findViewById(R.id.register_phone_et);
        passwordEt = registerFragment.findViewById(R.id.register_password_et);
        confirmPasswordEt = registerFragment.findViewById(R.id.register_confirm_password_et);
        streetAddressEt = registerFragment.findViewById(R.id.register_street_et);
        stateEt = registerFragment.findViewById(R.id.register_state_et);
        countryEt = registerFragment.findViewById(R.id.register_country_et);
        avatarImageView = registerFragment.findViewById(R.id.register_avatar_imv);
        registerBtn = registerFragment.findViewById(R.id.register_btn);
        clickForLogin = registerFragment.findViewById(R.id.register_login_tv);
        progressIndicator = registerFragment.findViewById(R.id.register_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
        avatarImageView.setOnClickListener(this::showCameraMenu);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        clickForLogin.setOnClickListener((v) -> Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment));

        return registerFragment;
    }


    private void uploadImage() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }


    private void saveUser() {
        String fullName = fullNameEt.getText().toString();
        String phone =  phoneEt.getText().toString();
        String email = emailEt.getText().toString();
        String street = streetAddressEt.getText().toString();
        String state = stateEt.getText().toString();
        String country = countryEt.getText().toString();
        String password =  passwordEt.getText().toString();
        String confirmPassword =  confirmPasswordEt.getText().toString();
        User user = new User(fullName, email, phone, street, state, country);
        Bitmap image = ((BitmapDrawable) avatarImageView.getDrawable()).getBitmap();
        if(!password.equals(confirmPassword)){
            Toast.makeText(getContext(), "Passwords not matches", Toast.LENGTH_SHORT).show();
        }
        else if(fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || image == null){
            Toast.makeText(getContext(), "Please fill the fields first", Toast.LENGTH_SHORT).show();
        }
        else {
            registerBtn.setEnabled(false);
            progressIndicator.show();
            try {
                Model.instance.uploadImage(image, user.getEmail() + ".jpg", (url -> {
                    user.setImageUrl(url);
                    Model.instance.register(() -> {
                        navController.navigate(R.id.action_registerFragment_to_loginFragment);
                    }, user, password);
                }));

            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarImageView.setImageBitmap(imageBitmap);
        }else if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                avatarImageView.setImageURI(selectedImageUri);
            }
        }
    }
}