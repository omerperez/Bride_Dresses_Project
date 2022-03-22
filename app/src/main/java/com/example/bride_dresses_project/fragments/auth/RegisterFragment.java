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

    EditText emailEt,fullNameEt, phoneEt, passwordEt, confirmPasswordEt,streetAddressEt, stateEt, countryEt;
    Button registerBtn;
    TextView clickForLoginTv;
    ImageView avatarImageView;
    String fileName;
    Uri imageToSave;
    String path;
    private CircularProgressIndicator progressIndicator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailEt = view.findViewById(R.id.register_email_et);
        fullNameEt = view.findViewById(R.id.register_name_et);
        phoneEt = view.findViewById(R.id.register_phone_et);
        passwordEt = view.findViewById(R.id.register_password_et);
        confirmPasswordEt = view.findViewById(R.id.register_confirm_password_et);
        streetAddressEt = view.findViewById(R.id.register_street_et);
        stateEt = view.findViewById(R.id.register_state_et);
        countryEt = view.findViewById(R.id.register_country_et);
        avatarImageView = view.findViewById(R.id.register_avatar_imv);

        registerBtn = view.findViewById(R.id.register_btn);
        clickForLoginTv = view.findViewById(R.id.register_login_tv);

        progressIndicator = view.findViewById(R.id.register_fragment_progress_indicator);

        avatarImageView.setOnClickListener(this::showCameraMenu);

        /*
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
         */

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        clickForLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void uploadImage() {

        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    private void saveUser() {
        registerBtn.setEnabled(false);
        progressIndicator.show();

        User user = new User();
        //user.setId(idEt.getText().toString());
        user.setFullName(fullNameEt.getText().toString());
        user.setEmail(emailEt.getText().toString());
        user.setPhone(phoneEt.getText().toString());
        user.setStreetAddress(streetAddressEt.getText().toString());
        user.setState(stateEt.getText().toString());
        user.setCountry(countryEt.getText().toString());

        BitmapDrawable drawable = (BitmapDrawable) avatarImageView.getDrawable();
        Bitmap image = drawable.getBitmap();
        Model.instance.uploadImage(image, user.getPhone(), new Model.uploadImageListener() {
            @Override
            public void onComplete(String url) {
                if(url != null){
                    user.setImageUrl(url);
                    Log.d("tag","frag");
                    Model.instance.register(user, passwordEt.getText().toString(),new Model.AddUserListener(){
                        @Override
                        public void onComplete() {
                            Log.d("tag","frag");
                            Navigation.findNavController(registerBtn).navigate(R.id.action_global_nav_home);
                        }
                    });
                }
            }
        });
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
    /*
    super.onActivityResult(requestCode, resultCode, data);
    CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if(resultCode == RESULT_OK){
        imageToSave = result.getUri();
        path = FileUtils.getPath(RegisterActivity.this, imageToSave);
        compressImage(path);
    }
    void compressImage(String path){
        Luban.compress(RegisterActivity.this,new File(path))
                .setMaxSize(50)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {}
                    @Override
                    public void onSuccess(File file) {
                        SimpleDateFormat format = new SimpleDateFormat("yyy_MM_dd_HH_mm_ss", Locale.CANADA);
                        Date now = new Date();
                        fileName = format.format(now);
                        Picasso.with(RegisterActivity.this).load(file).into(imgDesigner);
                    }
                    @Override
                    public void onError(Throwable e) {}
                });
    }
     */
}