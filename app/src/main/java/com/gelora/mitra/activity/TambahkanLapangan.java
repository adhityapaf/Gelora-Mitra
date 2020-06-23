package com.gelora.mitra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gelora.mitra.R;
import com.gelora.mitra.fragments.TimePickerFragment;

import java.util.ArrayList;

public class TambahkanLapangan extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    ImageView backButton;
    Button pilihJamButton;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahkan_lapangan);
        backButton = findViewById(R.id.back_arrow);
        pilihJamButton = findViewById(R.id.pilihJam_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pilihJamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    // Mengambil data dari TimePicker
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView jampilihan;
        jampilihan = findViewById(R.id.jamPilihanText);
        jampilihan.setText("Jam : "+hourOfDay+" Menit : "+minute);
        stringArrayList.add(jampilihan.getText().toString());
        Log.d("TimePicker", "onTimeSet: "+stringArrayList.size()+"Isi : "+stringArrayList.get(stringArrayList.size()-1));

    }
}
