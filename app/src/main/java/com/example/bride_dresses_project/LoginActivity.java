package com.example.bride_dresses_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText phone, password;
    Button loginBtn;
    TextView clickForRegister;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");
    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phone = findViewById(R.id.login_phone);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        clickForRegister = findViewById(R.id.login_click_here);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your mobile or password", Toast.LENGTH_SHORT).show();
                } else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phoneTxt)) {
                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);
                                if (getPassword.equals(passwordTxt)) {
                                    Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong Password Or Mobile Number", Toast.LENGTH_SHORT).show();

                                }
                                Toast.makeText(LoginActivity.this, "Phone or Email is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Phone or Email is already registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        clickForRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }
}