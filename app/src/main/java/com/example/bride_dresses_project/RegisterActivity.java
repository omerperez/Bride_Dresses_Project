package com.example.bride_dresses_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName, phone, password, confirmPassword;
    Button registerBtn;
    TextView clickForLogin;
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
}

