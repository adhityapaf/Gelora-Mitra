package com.gelora.mitra.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gelora.mitra.R;

public class WelcomeScreenActivity extends AppCompatActivity {
Button daftar, masuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
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
                Intent intent =  new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
