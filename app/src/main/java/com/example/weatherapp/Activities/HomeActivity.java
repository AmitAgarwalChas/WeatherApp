package com.example.weatherapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Adapters.ViewPagerAdapter;
import com.example.weatherapp.Common;
import com.example.weatherapp.Fragments.GraphFragment;
import com.example.weatherapp.Fragments.ListFragment;
import com.example.weatherapp.MapsActivity;
import com.example.weatherapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ImageView placePicker;
    TextView cityName;
    ViewPager viewPager;
    private FusedLocationProviderClient fusedLocationClient;
    Double lat = 0.0, lon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        initViews();
        if(checkNetwork()){
            getCurrentLocation();
        }
    }

    private boolean checkNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getApplicationContext())
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert  connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo==null || networkInfo.isFailover()){
                Toast.makeText(getApplicationContext(), "Kindly check your internet connection",
                        Toast.LENGTH_LONG).show();
                return false;
            }else{
                return true;
            }
        }
        return true;
    }

    private void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            String permissions[] ={Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, 1);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Common.location = location;

                            Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = gCoder.getFromLocation(lat, lon, 1);
                                Log.d("Location:" , lat+" / "+lon);
                                if(!addresses.isEmpty()){
                                    String city = addresses.get(0).getLocality();
                                    Common.cityName = city;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            searchCity();
                            if(Common.searchCityName == null){
                                cityName.setText(Common.cityName);
                            }else{
                                cityName.setText(Common.searchCityName);
                            }
                            setUpViewPager();

                        }
                    }
                });

    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ListFragment(), "Details");
        viewPagerAdapter.addFragment(new GraphFragment(), "Forecast");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void searchCity() {
        placePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                finish();
            }
        });
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        placePicker = findViewById(R.id.search_city);
        cityName = findViewById(R.id.tv_city);
        viewPager = findViewById(R.id.view_pager);
    }
}
