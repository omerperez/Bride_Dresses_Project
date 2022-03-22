package com.example.bride_dresses_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.bride_dresses_project.databinding.ActivityBaseBinding;
import com.example.bride_dresses_project.fragments.MapsFragment;
import com.example.bride_dresses_project.fragments.auth.LoginFragment;
import com.example.bride_dresses_project.fragments.dresses.AddDressFragment;
import com.example.bride_dresses_project.fragments.dresses.DressesListFragment;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.bride_dresses_project.R;

import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    ActivityBaseBinding binding;
    NavController navCtl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        replaceFragment(new DressesListFragment());
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home_page:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.menu_map:
                    replaceFragment(new MapsFragment());
                    break;
                case R.id.menu_create:
                    replaceFragment(new AddDressFragment());
                    break;
                case R.id.menu_myproduct:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.menu_log_out:
                    Intent intent = new Intent(this, UsersActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return true;
        });
//        navCtl = ((NavHost)getSupportFragmentManager().findFragmentById(R.id.base_navhost)).getNavController();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.base_navhost, fragment);
        fragmentTransaction.commit();
    }
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        super.onCreateOptionsMenu(menu);
////        getMenuInflater().inflate(R.menu.main, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        if(!super.onOptionsItemSelected(item)){
////            switch (item.getItemId()){
////                case R.id.menu_create:
////                    navCtl.navigate(R.id.action_global_createDressFragment);
////                    break;
////                case R.id.menu_home_page:
////                    navCtl.navigate(R.id.action_global_dressesListFragment);
////                    break;
////                case R.id.menu_map:
////                    navCtl.navigate(R.id.action_global_googleMapsFragment);
////                    break;
//////                case R.id.menu_my_product:
//////                    navCtl.navigate(R.id.action_global_createDressFragment);
//////                    break;
//////                case R.id.menu_log_out:
//////                    Model.instance.logout(() -> {
//////                        navCtl.navigate(R.id.action_global_loginFragment);
//////                    });
//////                    break;
//            }
//
//        }else{
//            return true;
//        }
//        return false;
//    }

}