package com.example.bride_dresses_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bride_dresses_project.model.Designer;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.ModelFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName, phone, password, confirmPassword,edStreetAddress, edState, edCountry;
    Button registerBtn;
    TextView clickForLogin;
    ImageView imgDesigner;
    String fileName;
    Uri imageToSave;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.register_full_name);
        phone = findViewById(R.id.register_phone);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);
        edStreetAddress = findViewById(R.id.edStreet);
        edState = findViewById(R.id.edState);
        edCountry = findViewById(R.id.edCountry);
        imgDesigner = findViewById(R.id.register_img_designer);

        registerBtn = findViewById(R.id.register_btn);
        clickForLogin = findViewById(R.id.register_click_here);

        imgDesigner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkCameraPermission()){
                    ActivityCompat.requestPermissions(
                           RegisterActivity.this,new String[]{
                                   Manifest.permission.CAMERA,
                                   Manifest.permission.READ_EXTERNAL_STORAGE,
                                   Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                   ,1
                           );
               }else{
                   takeImage();
               }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewDesigner();
            }
        });

        clickForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void CreateNewDesigner() {
        final String fullNameTxt = fullName.getText().toString();
        final String phoneTxt = phone.getText().toString();
        final String passwordTxt = password.getText().toString();
        final String confirmPasswordTxt = confirmPassword.getText().toString();
        final String streetTxt = edStreetAddress.getText().toString();
        final String stateTxt = edState.getText().toString();
        final String countryTxt = edCountry.getText().toString();

        Designer designer = new Designer(fullNameTxt,phoneTxt,
                passwordTxt, streetTxt, stateTxt, countryTxt, fileName);
        if (fullNameTxt.isEmpty() || passwordTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!passwordTxt.equals(confirmPasswordTxt)) {
            Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
        } else {
            Model.instance.createDesigner(designer, imageToSave, ()->{
                Toast.makeText(RegisterActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    public boolean checkCameraPermission(){
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED;

    }
    void takeImage(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if(resultCode == RESULT_OK){
            imageToSave = result.getUri();
            path = FileUtils.getPath(RegisterActivity.this, imageToSave);
            compressImage(path);
        }
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
}

