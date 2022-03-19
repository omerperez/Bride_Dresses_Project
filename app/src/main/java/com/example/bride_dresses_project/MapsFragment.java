package com.example.bride_dresses_project;

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

import com.example.bride_dresses_project.model.User;
import com.example.bride_dresses_project.model.ModelFirebase;
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

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        ModelFirebase modelFirebase;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            // LatLng sydney = new LatLng(-34, 151);
            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            getDataFromFirebase(googleMap);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("tag1", "permissions");
                return;
            }
            Log.d("tag1", "eden");

            googleMap.setMyLocationEnabled(true);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

    User designer;
    List<User> designersList = new ArrayList<>();

    void getDataFromFirebase(GoogleMap googleMap){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://bridedressesproject-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("designers");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                designer = new User(map.get("id"),
                        map.get("email"),
                        map.get("fullName"),
                        map.get("phone"),
                        map.get("streetAddress"),
                        map.get("state"),
                        map.get("country"));

                designersList.add(designer);
                Log.d("tag1", String.valueOf(designersList.size()));
                LatLng address = null;

                Log.d("tag1", String.valueOf(designersList.size()) + "deden");
                for (int i = 0; i < designersList.size(); i++) {
                    try {
                        String addr = designersList.get(i).getStreetAddress() + "," +
                                designersList.get(i).getState() + "," +
                                designersList.get(i).getCountry() + ",";
                        Log.d("tag1", addr);

                        address = getLatLongFromAddress(getActivity(), addr);
                        Log.d("tag1", address.toString());

                        googleMap.addMarker(new MarkerOptions().position(address)
                                .title(designersList.get(i).getStreetAddress()));

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
        Log.d("tag1", String.valueOf(latLng));

        return latLng;
    }
}

