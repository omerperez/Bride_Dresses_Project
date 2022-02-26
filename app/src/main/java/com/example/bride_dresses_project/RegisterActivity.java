package com.example.bride_dresses_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
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
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");

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
                    Log.d("tag","problem");

                    ActivityCompat.requestPermissions(
                           RegisterActivity.this,new String[]{
                                   Manifest.permission.CAMERA,
                                   Manifest.permission.READ_EXTERNAL_STORAGE,
                                   Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                   ,1
                           );
               }else{
                   Log.d("tag","take");
                   takeImage();
               }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Designer designer = new Designer();
                designer.setFullName(fullName.getText().toString());
                designer.setPhone(phone.getText().toString());
                designer.setPassword(password.getText().toString());
                designer.setStreetAddress(edStreetAddress.getText().toString());
                designer.setState(edState.getText().toString());
                designer.setCountry(edCountry.getText().toString());
                designer.setImage(fileName);

                final String fullNameTxt = fullName.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();

                if (fullNameTxt.isEmpty() || passwordTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneTxt)) {
                                Toast.makeText(RegisterActivity.this, "Phone or Email is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("designers").child(phone.getText().toString()).setValue(designer);
                                //databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);
                                Toast.makeText(RegisterActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        clickForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            Uri resulturi = result.getUri();
            String path = FileUtils.getPath(RegisterActivity.this,resulturi);
            compressImage(path);
        }
    }

    void compressImage(String path){
        Luban.compress(RegisterActivity.this,new File(path))
                .setMaxSize(50)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {

//                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        ByteArrayOutputStream b = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
//                        byte[] byteArray = b.toByteArray();
                        SimpleDateFormat format = new SimpleDateFormat("yyy_MM_dd_HH_mm_ss", Locale.CANADA);
                        Date now = new Date();
                        fileName = format.format(now);
                        Picasso.with(RegisterActivity.this).load(file).into(imgDesigner);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }
}

