package com.example.bride_dresses_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.bride_dresses_project.R;

public class BaseActivity extends AppCompatActivity {

//    private Uri mCropImageUri;
    BottomNavigationView navigationView;
    NavController navCtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getSupportActionBar().hide();
        NavHost navHost = (NavHost)getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtl = navHost.getNavController();

        View navView = findViewById(R.id.bottom_navigation);
        navView.setBackground(null);
        View placeholder = findViewById(R.id.menub_create);
        placeholder.setEnabled(false);

        NavigationUI.setupActionBarWithNavController(this, navCtl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item)){
            switch (item.getItemId()){
                case R.id.menub_my_product:
                    navCtl.navigate(R.id.action_global_dressesListFragment);
                    break;
                case R.id.menub_map:
                    navCtl.navigate(R.id.action_global_dressesListFragment);
                    break;
                case R.id.menub_create:
                    navCtl.navigate(R.id.action_global_createDressFragment);
                    break;
                case R.id.menub_logout:
                    navCtl.navigate(R.id.action_global_nav_home);
                    break;
            }

        }else{
            return true;
        }
        return false;
    }

    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
//        fragment.onActivityResult(requestCode, resultCode, data);
//    }

}