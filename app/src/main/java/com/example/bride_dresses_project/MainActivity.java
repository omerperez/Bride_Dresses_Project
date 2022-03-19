package com.example.bride_dresses_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bride_dresses_project.activities.BaseActivity;

public class MainActivity extends AppCompatActivity {

    Button designerBtn, clientBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        designerBtn = findViewById(R.id.main_designer_btn);
        clientBtn = findViewById(R.id.main_client_btn);

        designerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        clientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BaseActivity.class));
            }
        });

    }

}