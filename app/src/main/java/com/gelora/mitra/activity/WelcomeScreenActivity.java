package com.gelora.mitra.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gelora.mitra.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    Button daftar, masuk;
    private long backPressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
        daftar = findViewById(R.id.daftarButton);
        masuk = findViewById(R.id.masukButton);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tekan kembali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();

    }
}
