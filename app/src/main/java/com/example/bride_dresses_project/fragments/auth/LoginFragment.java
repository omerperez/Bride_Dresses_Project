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

    View loginFragment;
    EditText emailEt, passwordEt;
    Button loginBtn;
    TextView clickForRegister;
    CircularProgressIndicator progressIndicator;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loginFragment =  inflater.inflate(R.layout.fragment_login, container, false);
        emailEt = loginFragment.findViewById(R.id.login_email_ed);
        passwordEt = loginFragment.findViewById(R.id.login_password_ed);
        loginBtn = loginFragment.findViewById(R.id.login_login_btn);
        clickForRegister = loginFragment.findViewById(R.id.login_register_btn);
        progressIndicator = loginFragment.findViewById(R.id.login_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        clickForRegister.setOnClickListener((v) -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));

        return loginFragment;
    }

    public void signIn() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getContext(), "Please fill the fields first", Toast.LENGTH_SHORT).show();
        } else {
            try {
                loginBtn.setEnabled(false);
                clickForRegister.setEnabled(false);
                progressIndicator.show();
                Model.instance.login(email, password, () -> {
                    navController.navigate(R.id.action_loginFragment_to_nav_graph);
                }, errorMessage -> {
                    Snackbar.make(loginFragment, errorMessage, Snackbar.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}