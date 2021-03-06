package com.example.bride_dresses_project.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    View view;
    EditText emailEt, passwordEt;
    Button loginBtn;
    TextView registerBtn;
    NavController navController;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_login, container, false);
        emailEt = view.findViewById(R.id.login_email_ed);
        passwordEt = view.findViewById(R.id.login_password_ed);
        loginBtn = view.findViewById(R.id.login_login_btn);
        registerBtn = view.findViewById(R.id.login_register_btn);
        progressIndicator = view.findViewById(R.id.login_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
        progressIndicator.show();
        if((Model.instance.isSignedIn())){
           Model.instance.logout(() -> {});
        }
        progressIndicator.hide();
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        return view;
    }

    public void setEnabledStatus(Boolean status){
        loginBtn.setEnabled(status);
        registerBtn.setEnabled(status);
        emailEt.setEnabled(status);
        passwordEt.setEnabled(status);
    }

    public void signInUser(){
        setEnabledStatus(false);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        try {
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(getContext(), "Please fill the fields first", Toast.LENGTH_SHORT).show();
                setEnabledStatus(true);
            }
            else if(password.length() < 6 ){
                Toast.makeText(getContext(), "Password length 6 characters minimum", Toast.LENGTH_SHORT).show();
                setEnabledStatus(true);
            }
            else {
                progressIndicator.show();
                Model.instance.login(email, password ,() -> {
                            navController.navigate(R.id.action_loginFragment_to_nav_graph);
                });
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            setEnabledStatus(true);
            progressIndicator.hide();
        }
    }
}