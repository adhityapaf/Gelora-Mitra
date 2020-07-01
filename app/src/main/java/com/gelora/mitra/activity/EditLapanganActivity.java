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
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gelora.mitra.R;
import com.gelora.mitra.adapter.JamLapanganAdapter;
import com.gelora.mitra.fragments.TimePickerFragment;
import com.gelora.mitra.model.LapanganData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static com.gelora.mitra.adapter.LapanganAdapter.GAMBAR_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.HARGA_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.ID_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.JENIS_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.KATEGORI_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.NAMA_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.WAKTU_SEWA;

public class EditLapanganActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditLapanganActivity";
    private Uri mImageUri;
    private ProgressBar mProgressBar;
    ImageView backButton, gambarLapangan;
    Button pilihJamButton, pilihGambarButton, simpanButton;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayList<String> kategoriArray = new ArrayList<>();
    ArrayList<String> jenisArray = new ArrayList<>();
    EditText namaLapangan, hargaLapangan;
    Spinner kategoriSpinner, jenisSpinner;
    DatabaseReference pemilikLapanganRef, lapanganRef, lapanganCounter, kategoriRef, jenisRef, totalRef;
    int counterLapangan = 0;
    int counterTotalLapangan = 0;
    String kategoriPilihan, jenisPilihan;
    StorageTask mUploadTask;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    String imageDownloadUrl= "";
    String idLapanganIntent, namaLapanganIntent, kategoriLapanganIntent, jenisLapanganIntent, hargaLapanganIntent, gambarLapanganIntent;
    ArrayList<String> waktuSewaIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lapangan);
        lapanganRef = FirebaseDatabase.getInstance().getReference("lapangan");
        pemilikLapanganRef = FirebaseDatabase.getInstance().getReference().child("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        totalRef = pemilikLapanganRef.child("total_lapangan");
        lapanganCounter = FirebaseDatabase.getInstance().getReference().child("lapangan").child("lapangan_counter");
        kategoriRef = FirebaseDatabase.getInstance().getReference().child("kategori_lapangan");
        jenisRef = FirebaseDatabase.getInstance().getReference().child("jenis_lapangan");
        //
        Intent intent = getIntent();
        idLapanganIntent = intent.getStringExtra(ID_LAPANGAN);
        namaLapanganIntent = intent.getStringExtra(NAMA_LAPANGAN);
        kategoriLapanganIntent = intent.getStringExtra(KATEGORI_LAPANGAN);
        jenisLapanganIntent = intent.getStringExtra(JENIS_LAPANGAN);
        waktuSewaIntent = intent.getStringArrayListExtra(WAKTU_SEWA);
        hargaLapanganIntent = intent.getStringExtra(HARGA_LAPANGAN);
        gambarLapanganIntent = intent.getStringExtra(GAMBAR_LAPANGAN);
        //
        namaLapangan = findViewById(R.id.namaLapangan_field);
        kategoriSpinner = findViewById(R.id.kategoriLapangan_spinner);
        jenisSpinner = findViewById(R.id.jenislapangan_spinner);
        backButton = findViewById(R.id.back_arrow);
        simpanButton = findViewById(R.id.simpanLapangan_button);
        pilihJamButton = findViewById(R.id.pilihJam_button);
        hargaLapangan = findViewById(R.id.hargaLapangan_field);
        pilihGambarButton = findViewById(R.id.pilihGambar_button);
        gambarLapangan = findViewById(R.id.gambarLapangan_imageview);
        mProgressBar = findViewById(R.id.progressbarHorizontal);
        mProgressBar.setVisibility(View.GONE);
        kategoriArray.add(0, "--Pilih Kategori--");
        jenisArray.add(0, "--Pilih Jenis--");
        readData();
        populateSpinner();

        // set data dari intent
        namaLapangan.setText(namaLapanganIntent);
        // spinner udh keselect di populatespinner
        stringArrayList.clear();
        stringArrayList.addAll(waktuSewaIntent);
        initRecyclerView();
        hargaLapangan.setText(hargaLapanganIntent);
        Glide.with(this)
                .load(gambarLapanganIntent)
                .centerCrop()
                .placeholder(R.drawable.background_fragments)
                .into(gambarLapangan);

        //
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
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        simpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(namaLapangan.getText().toString())) {
                    namaLapangan.setError("Nama Lapangan Harus di Isi");
                    return;
                }
                if (TextUtils.isEmpty(hargaLapangan.getText().toString())) {
                    hargaLapangan.setError("Harga Lapangan Harus di Isi");
                    return;
                }

                if (kategoriSpinner.getSelectedItem().equals("--Pilih Kategori--")) {
                    Toast.makeText(EditLapanganActivity.this, "Silakan Pilih Kategori Lapangan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (jenisSpinner.getSelectedItem().equals("--Pilih Kategori--")) {
                    Toast.makeText(EditLapanganActivity.this, "Silakan Pilih Jenis Lapangan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stringArrayList.size() == 0) {
                    Toast.makeText(EditLapanganActivity.this, "Silakan isi Waktu Sewa Lapangan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!gambarLapangan.isShown()) {
                    Toast.makeText(EditLapanganActivity.this, "Silakan Pilih Gambar", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(EditLapanganActivity.this, "Proses Upload sedang berlangsung, mohon ditunggu.", Toast.LENGTH_SHORT).show();
                } else {

                    //
                    if (mImageUri != null){
                        mProgressBar.setVisibility(View.VISIBLE);
                        mStorageRef = FirebaseStorage.getInstance().getReference("foto_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("id_lapangan").child(String.valueOf(counterLapangan));
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("lapangan").child("id_lapangan").child(String.valueOf(counterLapangan)).child("gambar_lapangan");
                        final DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("id_lapangan").child(String.valueOf(counterLapangan)).child("gambar_lapangan");
                        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+ "." + getFileExtension(mImageUri));
                        mUploadTask = fileReference.putFile(mImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(0);
                                            }
                                        }, 2000);
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String downloadUil = uri.toString();
                                                mDatabaseRef.setValue(downloadUil);
                                                mDatabaseRef2.setValue(downloadUil);

                                            }
                                        });
                                        kategoriPilihan = kategoriSpinner.getSelectedItem().toString();
                                        jenisPilihan = jenisSpinner.getSelectedItem().toString();
                                        String namaLapanganString = namaLapangan.getText().toString();
                                        kategoriPilihan = kategoriSpinner.getSelectedItem().toString();
                                        jenisPilihan = jenisSpinner.getSelectedItem().toString();
                                        int hargaLapanganInt = Integer.parseInt(hargaLapangan.getText().toString());
                                        String UIDMitra = FirebaseAuth.getInstance().getUid();
                                        String idLapangan = String.valueOf(counterLapangan);
                                        lapanganCounter.setValue(counterLapangan);
                                        totalRef.setValue(counterTotalLapangan);
                                        DatabaseReference lapanganRefId = lapanganRef.child("id_lapangan").child(String.valueOf(counterLapangan));
                                        DatabaseReference pemilikLapanganRefID = pemilikLapanganRef.child("id_lapangan").child(String.valueOf(counterLapangan));
                                        String gambarImageString = imageDownloadUrl;
                                        //
                                        LapanganData lapanganData = new LapanganData(
                                                idLapangan,
                                                namaLapanganString,
                                                gambarImageString,
                                                hargaLapanganInt,
                                                kategoriPilihan,
                                                jenisPilihan,
                                                UIDMitra
                                        );
                                        lapanganRefId.setValue(lapanganData);
                                        pemilikLapanganRefID.setValue(lapanganData);
                                        DatabaseReference jamRef = FirebaseDatabase.getInstance().getReference("lapangan").child("id_lapangan").child(String.valueOf(counterLapangan)).child("jam_sewa");
                                        DatabaseReference jamPemilikRef = pemilikLapanganRef.child("id_lapangan").child(String.valueOf(counterLapangan)).child("jam_sewa");
                                        for (int i = 0; i < stringArrayList.size(); i++) {
                                            jamRef.child(stringArrayList.get(i)).setValue("tersedia");
                                            jamPemilikRef.child(stringArrayList.get(i)).setValue("tersedia");
                                        }
                                        Log.d(TAG, "onSuccess: Success Upload");
                                        Toast.makeText(EditLapanganActivity.this, "Tambahkan Lapangan Berhasil!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditLapanganActivity.this, "Ups Terjadi Kesalahan : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        mProgressBar.setProgress((int) progress);
                                    }
                                });
                    } else {
                        Toast.makeText(EditLapanganActivity.this, "Anda Belum Memilih Gambar", Toast.LENGTH_SHORT).show();
                    }


                }
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
        lapanganCounter.keepSynced(true);
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counterTotalLapangan = Integer.parseInt(snapshot.getValue().toString());
                } else {
                    counterTotalLapangan = 0;
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
            kategoriSpinner.setSelection(adapterKategori.getPosition(kategoriLapanganIntent));
        }
        if (jenisSpinner != null) {
            jenisSpinner.setAdapter(adapterJenis);
            jenisSpinner.setSelection(adapterJenis.getPosition(jenisLapanganIntent));
        }
    }

    // Mengambil data dari TimePicker
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String pilihanJam;
        if (minute<10){
            pilihanJam = hourOfDay + ":0" +minute;
        } else {
            pilihanJam = hourOfDay + ":" +minute;
        }
        stringArrayList.add(pilihanJam);
        initRecyclerView();
        Log.d("TimePicker", "onTimeSet: " + stringArrayList.size() + "Isi : " + stringArrayList.get(stringArrayList.size() - 1));
    }

    // Membuat recycler view untuk jam
    private void initRecyclerView() {
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this).load(mImageUri).into(gambarLapangan);
            gambarLapangan.setVisibility(View.VISIBLE);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
