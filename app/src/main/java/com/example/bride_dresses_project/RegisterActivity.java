package com.example.bride_dresses_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bride_dresses_project.databinding.ActivityMainBinding;
import com.example.bride_dresses_project.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog pd;

    EditText fullName, phone, password, confirmPassword;
    Button registerBtn;
    TextView clickForLogin;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registerAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        fullName = findViewById(R.id.register_full_name);
        phone = findViewById(R.id.register_phone);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);

        registerBtn = findViewById(R.id.register_btn);
        clickForLogin = findViewById(R.id.register_click_here);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                databaseReference.child("users").child(phoneTxt).child("fullName").setValue(fullNameTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);
                                uploadImageToFirebase();
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && requestCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            binding.registerAddImage.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase() {
        pd = new ProgressDialog(this);
        pd.setTitle("Uploaded File...");
        pd.show();

        SimpleDateFormat format = new SimpleDateFormat("yyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = format.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                if(pd.isShowing()){
                    pd.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(pd.isShowing()){
                    pd.dismiss();
                }
                Toast.makeText(RegisterActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

