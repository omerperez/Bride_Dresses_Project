package com.example.bride_dresses_project.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Model;
import com.example.bride_dresses_project.model.entities.User;
import com.example.bride_dresses_project.viewModel.UsersListViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

    UsersListViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UsersListViewModel.class);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d("tag1", "map ready");

            // LatLng sydney = new LatLng(-34, 151);
            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            viewModel.getUsersList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                    LatLng address = null;

                    for (int i = 0; i < users.size(); i++) {
                        try {
                            String addr = users.get(i).getStreetAddress() + "," +
                                    users.get(i).getState() + "," +
                                    users.get(i).getCountry() + ",";
                            Log.d("tag1", addr);

                            address = getLatLongFromAddress(getActivity(), addr);
                            Log.d("tag1", "bdika"+address.toString());

                            googleMap.addMarker(new MarkerOptions().position(address)
                                    .title(users.get(i).getStreetAddress()));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(address));

                        } catch (Exception e) {

                        }
                    }
                }
            });

            /*
            Model.getAllUsers(new Model.GetAllUsersListener() {
                @Override
                public void onComplete(List<User> result) {
                    LatLng address = null;
                    Log.d("tag1", "here2");

                    for (int i = 0; i < result.size(); i++) {
                        try {
                            String addr = result.get(i).getStreetAddress() + "," +
                                    result.get(i).getState() + "," +
                                    result.get(i).getCountry() + ",";
                            Log.d("tag1", addr);

                            address = getLatLongFromAddress(getActivity(), addr);
                            Log.d("tag1", address.toString());

                            googleMap.addMarker(new MarkerOptions().position(address)
                                    .title(result.get(i).getStreetAddress()));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(address));



                        } catch (Exception e) {

                        }
                    }
                }
            });

             */
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("tag1", "permissions");
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("tag1", "onCreateView here");

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }



/*
    void getDataFromFirebase(GoogleMap googleMap){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://bridedressesproject-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("designers");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                user = new User(map.get("email"),
                        map.get("fullName"),
                        map.get("phone"),
                        map.get("streetAddress"),
                        map.get("state"),
                        map.get("country"));

                usersList.add(user);
                Log.d("tag1", String.valueOf(usersList.size()));
                LatLng address = null;

                Log.d("tag1", String.valueOf(usersList.size()) + "deden");
                for (int i = 0; i < usersList.size(); i++) {
                    try {
                        String addr = usersList.get(i).getStreetAddress() + "," +
                                usersList.get(i).getState() + "," +
                                usersList.get(i).getCountry() + ",";
                        Log.d("tag1", addr);

                        address = getLatLongFromAddress(getActivity(), addr);
                        Log.d("tag1", address.toString());

                        googleMap.addMarker(new MarkerOptions().position(address)
                                .title(usersList.get(i).getStreetAddress()));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(address));

                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


 */


    LatLng getLatLongFromAddress(Context context,String strAddress){
        Geocoder geocoder = new Geocoder(context);
        List<Address> address;
        LatLng latLng=null;
        try{
            address = geocoder.getFromLocationName(strAddress,2);
            if(address== null){
                Log.d("tag1","not found");
                return null;
            }
            Address loc = address.get(0);
            latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        }catch (Exception e){}

        return latLng;
    }
}