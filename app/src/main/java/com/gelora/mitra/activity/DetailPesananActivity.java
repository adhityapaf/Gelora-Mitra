package com.gelora.mitra.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gelora.mitra.R;
import com.gelora.mitra.adapter.PesananAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.gelora.mitra.adapter.LapanganAdapter.ID_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.NAMA_LAPANGAN;
import static com.gelora.mitra.adapter.PesananAdapter.ALASAN_PESANAN;
import static com.gelora.mitra.adapter.PesananAdapter.BUKTI_PEMBAYARAN;
import static com.gelora.mitra.adapter.PesananAdapter.ID_PESANAN;
import static com.gelora.mitra.adapter.PesananAdapter.JAM_PESANAN;
import static com.gelora.mitra.adapter.PesananAdapter.NAMA_PEMESAN;
import static com.gelora.mitra.adapter.PesananAdapter.STATUS_PESANAN;
import static com.gelora.mitra.adapter.PesananAdapter.TANGGAL_PESANAN;
import static com.gelora.mitra.adapter.PesananAdapter.TANGGAL_PESAN_USER;
import static com.gelora.mitra.adapter.PesananAdapter.TOTAL_HARGA;
import static com.gelora.mitra.adapter.PesananAdapter.UID_MITRA;
import static com.gelora.mitra.adapter.PesananAdapter.UID_PELANGGAN;

public class DetailPesananActivity extends AppCompatActivity {
    TextView idTransaksi, tanggalPesan, waktuPesan, namaPemesan, namaLapangan, totalHarga, statusPesanan;
    ImageView statusIcon;
    Button buktiTransferButton, tolakButton, terimaButton;
    String idTransaksiIntent, namaPemesanIntent, buktiPembayaranIntent, jamPesanIntent, tanggalPesanIntent, statusPesanIntent, namaLapanganIntent, alasanPesananIntent, UIDMitraIntent, UIDPelangganIntent, tanggalPesanUserIntent, idLapanganIntent;
    int totalHargaIntent;
    String forUploadText = "belum ada";
    String alasanDefault = "Tidak Ada";
    Locale locale = new Locale("id", "ID");
    NumberFormat n = NumberFormat.getCurrencyInstance(locale);
    String s, a;
    DatabaseReference penggunaRef, pemilikRef;
    String statuspesananTerimaString = "Pesanan Telah di Terima.";
    String statuspesananTolakString = "Pesanan Telah di Tolak.";
    long totalpenghasilan = 0;
    DatabaseReference totalPenghasilanRef;
    String alasanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        idTransaksi = findViewById(R.id.idTransaksi_field);
        tanggalPesan = findViewById(R.id.tanggalPesan_field);
        waktuPesan = findViewById(R.id.waktuPesan_field);
        namaPemesan = findViewById(R.id.namaPemesan_field);
        namaLapangan = findViewById(R.id.namaLapanganPesanan_field);
        totalHarga = findViewById(R.id.totalHarga_amount);
        statusPesanan = findViewById(R.id.statusPesananText);
        statusIcon = findViewById(R.id.statusIcon);
        buktiTransferButton = findViewById(R.id.lihatBuktiTransfer_button);
        tolakButton = findViewById(R.id.tolak_button);
        terimaButton = findViewById(R.id.verifikasi_button);

        retrieveIntent();
        settingText();
        totalPenghasilanRef = FirebaseDatabase.getInstance().getReference("pesanan_pemilik").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("total_penghasilan");
        readData();

        penggunaRef = FirebaseDatabase.getInstance().getReference("pesanan").child(UIDPelangganIntent).child(tanggalPesanUserIntent).child("id_pesanan").child(idTransaksiIntent);
        pemilikRef = FirebaseDatabase.getInstance().getReference("pesanan_pemilik").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(tanggalPesanIntent).child("id_pesanan").child(idTransaksiIntent);

        // membuat tampilan seperti pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);

        buktiTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPesananActivity.this, ImagePreviewActivity.class);
                intent.putExtra(BUKTI_PEMBAYARAN, buktiPembayaranIntent);
                startActivity(intent);
            }
        });

        if (alasanPesananIntent.equals(statuspesananTerimaString)) {
            terimaButton.setVisibility(View.INVISIBLE);
            tolakButton.setVisibility(View.INVISIBLE);
            statusPesanan.setVisibility(View.VISIBLE);
            statusIcon.setVisibility(View.VISIBLE);
        } else if (alasanPesananIntent.equals(statuspesananTolakString)) {
            terimaButton.setVisibility(View.INVISIBLE);
            tolakButton.setVisibility(View.INVISIBLE);
            statusPesanan.setVisibility(View.VISIBLE);
            statusIcon.setVisibility(View.VISIBLE);
            statusPesanan.setTextColor(Color.RED);
            Glide.with(DetailPesananActivity.this)
                    .load(R.drawable.ic_ditolak)
                    .into(statusIcon);
        }

        terimaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(DetailPesananActivity.this).create();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananActivity.this);
                builder.setTitle("Terima Pesanan");
                builder.setMessage("Apakah Anda yakin untuk menerima pesanan ini?");
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                builder.setPositiveButton("Terima", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pemilikRef.child("alasan_status").setValue(statuspesananTerimaString);
                        penggunaRef.child("alasan_status").setValue("Pesanan Diterima, Selamat Bermain!");
                        penggunaRef.child("status_pesanan").setValue("Diterima");
                        pemilikRef.child("status_pesanan").setValue("Diterima");
                        terimaButton.setVisibility(View.INVISIBLE);
                        tolakButton.setVisibility(View.INVISIBLE);
                        statusPesanan.setVisibility(View.VISIBLE);
                        statusPesanan.setText(statuspesananTerimaString);
                        statusIcon.setVisibility(View.VISIBLE);
                        totalpenghasilan = totalHargaIntent + totalpenghasilan;
                        totalPenghasilanRef.setValue(totalpenghasilan);
                    }
                });
                builder.show();
            }
        });

        tolakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(DetailPesananActivity.this).create();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananActivity.this);
                builder.setTitle("Alasan Anda Menolak Pesanan ini?");
                final EditText alasanEdit = new EditText(DetailPesananActivity.this);
                alasanEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(alasanEdit);
                builder.setPositiveButton("Tolak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alasanText = alasanEdit.getText().toString();
                        if (alasanText.equals("")) {
                            Toast.makeText(DetailPesananActivity.this, "Alasan masih kosong, silakan diisi untuk menolak pesanan", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            setAvailablityLapangan();
                            pemilikRef.child("alasan_status").setValue(statuspesananTolakString);
                            penggunaRef.child("alasan_status").setValue(alasanText);
                            penggunaRef.child("status_pesanan").setValue("Ditolak");
                            pemilikRef.child("status_pesanan").setValue("Ditolak");
                            terimaButton.setVisibility(View.INVISIBLE);
                            tolakButton.setVisibility(View.INVISIBLE);
                            statusPesanan.setVisibility(View.VISIBLE);
                            statusPesanan.setText(statuspesananTolakString);
                            statusIcon.setVisibility(View.VISIBLE);
                            Glide.with(DetailPesananActivity.this)
                                    .load(R.drawable.ic_ditolak)
                                    .into(statusIcon);
                        }
                    }

                });
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    private void setAvailablityLapangan() {
        DatabaseReference ketersediaanRef;
        ketersediaanRef = FirebaseDatabase.getInstance().getReference("ketersediaan_lapangan").child(idLapanganIntent).child(tanggalPesanIntent);
        String[] splitString = jamPesanIntent.split(", ");
        List<String> jamArrayList = new ArrayList<>();
        jamArrayList = Arrays.asList(splitString);
        for (String s : jamArrayList){
            ketersediaanRef.child(s).setValue("tersedia");
        }
    }

    private void readData() {
        totalPenghasilanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalpenghasilan = (long) snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void settingText() {
        idTransaksi.setText(idTransaksiIntent);
        tanggalPesan.setText(tanggalPesanIntent);
        waktuPesan.setText(jamPesanIntent);
        namaPemesan.setText(namaPemesanIntent);
        namaLapangan.setText(namaLapanganIntent);
        totalHarga.setText(a);
        statusPesanan.setText(statusPesanIntent);
        statusIcon.setVisibility(View.INVISIBLE);
        statusPesanan.setVisibility(View.INVISIBLE);
    }

    private void retrieveIntent() {
        Intent intent = getIntent();
        idTransaksiIntent = intent.getStringExtra(ID_PESANAN);
        namaPemesanIntent = intent.getStringExtra(NAMA_PEMESAN);
        totalHargaIntent = intent.getIntExtra(TOTAL_HARGA, 0);
        buktiPembayaranIntent = intent.getStringExtra(BUKTI_PEMBAYARAN);
        jamPesanIntent = intent.getStringExtra(JAM_PESANAN);
        tanggalPesanIntent = intent.getStringExtra(TANGGAL_PESANAN);
        statusPesanIntent = intent.getStringExtra(STATUS_PESANAN);
        namaLapanganIntent = intent.getStringExtra(NAMA_LAPANGAN);
        alasanPesananIntent = intent.getStringExtra(ALASAN_PESANAN);
        UIDMitraIntent = intent.getStringExtra(UID_MITRA);
        UIDPelangganIntent = intent.getStringExtra(UID_PELANGGAN);
        tanggalPesanUserIntent = intent.getStringExtra(TANGGAL_PESAN_USER);
        idLapanganIntent = intent.getStringExtra(ID_LAPANGAN);
        s = n.format(totalHargaIntent);
        a = s.replaceAll(",00", "").replaceAll("Rp", "Rp. ");
    }
}