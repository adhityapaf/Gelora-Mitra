package com.gelora.mitra.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.gelora.mitra.R;
import com.gelora.mitra.adapter.JamLapanganAdapter;
import com.gelora.mitra.fragments.TimePickerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TambahkanLapangan extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "TambahkanLapangan";
    private Uri mImageUri;
    ImageView backButton, gambarLapangan;
    Button pilihJamButton, pilihGambarButton, simpanButton;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayList<String> kategoriArray = new ArrayList<>();
    ArrayList<String> jenisArray = new ArrayList<>();
    EditText namaLapangan, hargaLapangan;
    Spinner kategoriSpinner, jenisSpinner;
    DatabaseReference pemilikLapanganRef, lapanganRef, lapanganCounter, kategoriRef, jenisRef;
    int counterLapangan = 0;
    String kategoriPilihan, jenisPilihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahkan_lapangan);
        lapanganRef = FirebaseDatabase.getInstance().getReference("Lapangan");
        pemilikLapanganRef = FirebaseDatabase.getInstance().getReference().child("pemilik_lapangan").child(FirebaseAuth.getInstance().getUid());
        lapanganCounter = FirebaseDatabase.getInstance().getReference().child("lapangan").child("lapangan_counter");
        kategoriRef = FirebaseDatabase.getInstance().getReference().child("kategori_lapangan");
        jenisRef = FirebaseDatabase.getInstance().getReference().child("jenis_lapangan");
        namaLapangan = findViewById(R.id.namaLapangan_field);
        kategoriSpinner = findViewById(R.id.kategoriLapangan_spinner);
        jenisSpinner = findViewById(R.id.jenislapangan_spinner);
        backButton = findViewById(R.id.back_arrow);
        simpanButton = findViewById(R.id.simpanLapangan_button);
        pilihJamButton = findViewById(R.id.pilihJam_button);
        hargaLapangan = findViewById(R.id.hargaLapangan_field);
        pilihGambarButton = findViewById(R.id.pilihGambar_button);
        gambarLapangan = findViewById(R.id.gambarLapangan_imageview);
        kategoriArray.add(0, "--Pilih Kategori--");
        jenisArray.add(0, "--Pilih Jenis--");
        readData();
        populateSpinner();

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

        pilihGambarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        simpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategoriPilihan = kategoriSpinner.getSelectedItem().toString();
                jenisPilihan = jenisSpinner.getSelectedItem().toString();
                System.out.println(kategoriPilihan);
                System.out.println(jenisPilihan);
            }
        });
    }

    // read data
    private void readData() {
        lapanganCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    counterLapangan = Integer.parseInt(snapshot.getValue().toString());
                } else {
                    counterLapangan = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        kategoriRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        kategoriArray.add(ds.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        jenisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String jenisValues = ds.getValue(String.class);
                        jenisArray.add(jenisValues);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // mengisi Spinner
    public void populateSpinner() {
        ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriArray);
        ArrayAdapter<String> adapterJenis = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisArray);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterJenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (kategoriSpinner != null) {
            kategoriSpinner.setAdapter(adapterKategori);
        }
        if (jenisSpinner != null) {
            jenisSpinner.setAdapter(adapterJenis);
        }
    }

    // Mengambil data dari TimePicker
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView jampilihan;
        jampilihan = findViewById(R.id.jamPilihanText);
        jampilihan.setVisibility(View.GONE);
        jampilihan.setText(hourOfDay + ":" + minute);
        stringArrayList.add(jampilihan.getText().toString());
        initRecyclerView();
        Log.d("TimePicker", "onTimeSet: " + stringArrayList.size() + "Isi : " + stringArrayList.get(stringArrayList.size() - 1));
    }

    // Membuat recycler view untuk jam
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: Created");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.waktuSewa_recycler);
        recyclerView.setLayoutManager(layoutManager);
        JamLapanganAdapter adapter = new JamLapanganAdapter(stringArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    // milih gambar
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Glide.with(this).load(mImageUri).into(gambarLapangan);
            gambarLapangan.setVisibility(View.VISIBLE);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
