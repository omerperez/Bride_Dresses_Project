package com.example.bride_dresses_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends Fragment {

    EditText phone, password;
    Button loginBtn;
    TextView clickForRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        phone = view.findViewById(R.id.login_phone);
        password = view.findViewById(R.id.login_password);
        loginBtn = view.findViewById(R.id.login_login_btn);
        clickForRegister = view.findViewById(R.id.login_click_here);

        return view;
    }
}