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
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class LoginFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button loginBtn;
    TextView registerBtn;
    NavController navController;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_login, container, false);
//        if(!Model.instance.isSignedIn()){
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
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginBtn.setEnabled(false);
                    registerBtn.setEnabled(false);
                    progressIndicator.show();
                    Model.instance.login(
                            emailEt.getText().toString(),
                            passwordEt.getText().toString(),() -> {
                                navController.navigate(R.id.action_loginFragment_to_nav_graph);
//                                @Override
//                                public void onComplete() {
//                                    Navigation.findNavController(loginBtn).navigate(R.id.action_loginFragment_to_nav_graph);
//                                }
                            }
                    );
            }
        });
//}
        return view;
    }



}