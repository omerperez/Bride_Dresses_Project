package com.example.bride_dresses_project.fragments.dresses.users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Model;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    EditText emailEt, passwordEt;
    Button loginBtn;
    TextView registerBtn;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        emailEt = view.findViewById(R.id.login_email_ed);
        passwordEt = view.findViewById(R.id.login_password_ed);
        loginBtn = view.findViewById(R.id.login_login_btn);
        registerBtn = view.findViewById(R.id.login_register_btn);

        progressIndicator = view.findViewById(R.id.login_fragment_progress_indicator);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setErrorIfEmailIsInvalid();
                //setErrorIfPasswordIsInvalid();
                if (true) {
                    loginBtn.setEnabled(false);
                    registerBtn.setEnabled(false);
                    progressIndicator.show();
                    Model.instance.login(
                            emailEt.getText().toString(),
                            passwordEt.getText().toString(),new Model.LoginListener(){
                                @Override
                                public void onComplete() {
                                    Log.d("tag","complete");
                                    Navigation.findNavController(loginBtn).navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph());
                                }
                            }
                    );

                    /*
                            () -> {
                                                                Navigation.findNavController(loginBtn).navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph());
                                Navigation.findNavController(loginBtn).navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph());
                            },
                            errorMessage -> {
                                Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
                                loginBtn.setEnabled(true);
                                registerBtn.setEnabled(true);
                                progressIndicator.hide();
                            });

                     */
                }
            }
        });


        return view;
    }





}