package com.example.bride_dresses_project.activities;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import com.example.bride_dresses_project.NavGraphDirections;
import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.databinding.ActivityBaseBinding;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBaseBinding binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home_page:
                    Navigation.findNavController(this, R.id.base_navhost).navigate(NavGraphDirections.actionGlobalDressesListFragment());
                    break;
                case R.id.menu_map:
                    break;
                case R.id.menu_create:
                    Navigation.findNavController(this, R.id.base_navhost).navigate(NavGraphDirections.actionGlobalCreateDressFragment(null));
                    break;
                case R.id.menu_myproduct:
                    break;
                case R.id.menu_log_out:
                    Intent intent = new Intent(this, UsersActivity.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    break;
            }
            return true;
        });
    }
}