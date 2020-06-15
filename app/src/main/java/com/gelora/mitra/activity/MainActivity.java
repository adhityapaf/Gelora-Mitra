package com.gelora.mitra.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.gelora.mitra.R;
import com.gelora.mitra.fragments.AkunFragment;
import com.gelora.mitra.fragments.DashboardFragment;
import com.gelora.mitra.fragments.LapanganFragment;
import com.gelora.mitra.fragments.PesananFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DashboardFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_dashboard:
                    selectedFragment = new DashboardFragment();
                    break;
                case R.id.nav_lapangan:
                    selectedFragment = new LapanganFragment();
                    break;
                case R.id.nav_pesanan:
                    selectedFragment = new PesananFragment();
                    break;
                case R.id.nav_akun:
                    selectedFragment = new AkunFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}
