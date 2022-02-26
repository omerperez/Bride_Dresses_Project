package com.example.bride_dresses_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bride_dresses_project.model.Dress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bride_dresses_project.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText price, type;
    ImageView dressImage;
    Button saveBtn;
    String fileName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReferenceFromUrl("https://bridedressesproject-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        price=findViewById(R.id.create_dress_price);
        type=findViewById(R.id.create_dress_type);
        dressImage=findViewById(R.id.create_dress_image);
        saveBtn=findViewById(R.id.create_dress_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String priceTxt = price.getText().toString();
                final  String typeTxt = type.getText().toString();
//                Dress dress = new Dress(priceTxt,typeTxt,fileName);
                Dress dress = new Dress(priceTxt,typeTxt, "Linoy Bekker");

                if (priceTxt.isEmpty() || typeTxt.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (priceTxt.indexOf("-")!=-1) {
                    Toast.makeText(getContext(), "Price is Illegal", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("dresses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(dress.getId())) {
                                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("dresses").child(dress.getId()).setValue(dress);
                                Toast.makeText(getContext(), "Dress Created successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });

                }
            }
        });

    }

    private Context getContext() {
        return MainActivity.this;
    }


    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
//
////        binding = ActivityMainBinding.inflate(getLayoutInflater());
////        setContentView(binding.getRoot());
//
//        navigationView = findViewById(R.id.bottom_navigation);
////        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomePageFragment()).commit();
////        navigationView.getSelectedItemId(R.id.miHome);
//
//        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
//                switch (item.getItemId()){
//                    case R.id.miHome:
////                        fragment = new HomePageFragment();
////                        break;
//                    case R.id.miProfile:
////                        fragment = new ProfileFragment();
////                        break;
//
//                    case R.id.miSearch:
////                        fragment = new SearchFragment();
////                        break;
//
//                    case R.id.miSettings:
////                        fragment = new SettingsFragment();
////                        break;
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
//
//                return true;
//            }
//        });
//
//        View navView = findViewById(R.id.bottom_navigation);
//        navView.setBackground(null);
//        View placeholder = findViewById(R.id.placeholder);
//        placeholder.setEnabled(false);
//
//        navController = Navigation.findNavController(this, R.id.main_navhost);
//        NavigationUI.setupActionBarWithNavController(this, navController);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.bottom_nav_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home){
//            navController.navigateUp();
//            return true;
//        }else{
//            return NavigationUI.onNavDestinationSelected(item, navController);
//        }
//    }
//    private AppBarConfiguration mAppBarConfiguration;
//
//    private ActivityMainBinding binding;
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        DrawerLayout drawer = binding.drawerLayout;
//        NavigationView navigationView = binding.navView;
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setOpenableLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}