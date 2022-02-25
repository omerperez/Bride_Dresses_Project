package com.example.bride_dresses_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    EditText fullName, email, phone, password, confirmPassword;
    Button registerBtn;
    TextView clickForLogin;
//    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullName = findViewById(R.id.register_full_name);
        email = findViewById(R.id.register_email);
        phone = findViewById(R.id.register_phone);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);

        registerBtn = findViewById(R.id.register_btn);
        clickForLogin = findViewById(R.id.register_click_here);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullNameTxt = fullName.getText().toString();
                final String emailTxt = email.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();

                if (fullNameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneTxt) || snapshot.hasChild(emailTxt)) {
                                Toast.makeText(RegisterActivity.this, "Phone or Email is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").child(phoneTxt).child("fullName").setValue(fullNameTxt);
                                databaseReference.child("users").child(phoneTxt).child("email").setValue(emailTxt);
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

//}
//
//    private void PerformAuth() {
//        String email = inputEmail.getText().toString();
//        String password = inputPassword.getText().toString();
//        String conformPassword = inputConformPassword.getText().toString();
//
//        if(!email.matches(emailPattern)){
//            inputEmail.setError("Please enter a valid email address");
//        }else if(password.isEmpty() || password.length() < 6){
//            inputPassword.setError("Please Enter a valid password");
//        }else if(!password.equals(conformPassword)){
//            inputConformPassword.setError("The passwords are not the same");
//        }else{
//            progressDialog.setMessage("Pleas Wait While Registration...");
//            progressDialog.setTitle("Registration");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//
//            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        sendUserToNextActivity();
//                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                    }else{
//                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
//
//    private void sendUserToNextActivity() {
//        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

