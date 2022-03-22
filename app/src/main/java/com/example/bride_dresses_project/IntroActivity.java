package com.example.bride_dresses_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bride_dresses_project.activities.BaseActivity;
import com.example.bride_dresses_project.activities.UsersActivity;
import com.example.bride_dresses_project.model.Model;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Model.instance.isSignedIn()){
                Model.instance.mainThread.post(() -> {
                    toBaseActivity();
                });
            }else{
                Model.instance.mainThread.post(() -> {
                    toAuthActivity();
                });
            }
        });
    }

    private void toAuthActivity() {
        Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
        finish();
    }

    private void toBaseActivity() {
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
        finish();
    }
}

