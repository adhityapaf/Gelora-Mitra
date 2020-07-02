package com.gelora.mitra.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gelora.mitra.R;
import com.gelora.mitra.activity.EditLapanganActivity;
import com.gelora.mitra.model.LapanganData;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {

    ArrayList<LapanganData> lapanganData;
    Context mContext;
    DatabaseReference ref;
    HashMap<String, String> hashMap = new HashMap<String,String>();
    ArrayList<String> jamSewaArrayList;
    public static final String ID_LAPANGAN = "com.gelora.mitra.id_lapangan";
    public static final String NAMA_LAPANGAN = "com.gelora.mitra.nama_lapangan";
    public static final String KATEGORI_LAPANGAN = "com.gelora.mitra.kategori_lapangan";
    public static final String JENIS_LAPANGAN = "com.gelora.mitra.jenis_lapangan";
    public static final String WAKTU_SEWA = "com.gelora.mitra.waktu_sewa";
    public static final String HARGA_LAPANGAN = "com.gelora.mitra.harga_lapangan";
    public static final String GAMBAR_LAPANGAN = "com.gelora.mitra.gambar_lapangan";

    public LapanganAdapter(ArrayList<LapanganData> lapanganData, Context mContext ) {
        this.lapanganData = lapanganData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LapanganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_holder_lapangan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(lapanganData.get(position).getGambar_lapangan())
                .centerCrop()
                .placeholder(R.drawable.background_fragments)
                .into(holder.gambarLapangan);
        holder.namaLapangan.setText(lapanganData.get(position).getNama_lapangan());
        holder.kategoriLapangan.setText(lapanganData.get(position).getKategori_lapangan());
        holder.jenisLapangan.setText(lapanganData.get(position).getJenis_lapangan());
        String bilanganHarga = String.valueOf(lapanganData.get(position).getHarga());
        holder.hargaSewa.setText("Rp. "+bilanganHarga);
        hashMap = lapanganData.get(position).getJam_sewa();
        Set<String> jamKeySet = hashMap.keySet();
        jamSewaArrayList = new ArrayList<>(jamKeySet);
        System.out.println(jamSewaArrayList);
        holder.jamSewaRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.jamSewaRecycler.setLayoutManager(layoutManager);
        JamSewaAdapter jamSewaAdapter = new JamSewaAdapter(jamSewaArrayList, mContext);
        holder.jamSewaRecycler.setAdapter(jamSewaAdapter);
        holder.editLapangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(mContext, EditLapanganActivity.class);
                String id_lapanganStr, namaLapanganStr, kategoriLapanganStr, jenisLapanganStr, hargaLapanganStr, gambarLapanganStr;
                namaLapanganStr = holder.namaLapangan.getText().toString();
                kategoriLapanganStr = holder.kategoriLapangan.getText().toString();
                jenisLapanganStr = holder.jenisLapangan.getText().toString();
                id_lapanganStr = lapanganData.get(position).getId_lapangan();
                ArrayList<String> jamList;
                jamList = jamSewaArrayList;
                hargaLapanganStr = holder.hargaSewa.getText().toString();
                gambarLapanganStr = lapanganData.get(position).getGambar_lapangan();
                intent.putExtra(ID_LAPANGAN, id_lapanganStr);
                intent.putExtra(NAMA_LAPANGAN, namaLapanganStr);
                intent.putExtra(KATEGORI_LAPANGAN, kategoriLapanganStr);
                intent.putExtra(JENIS_LAPANGAN, jenisLapanganStr);
                intent.putExtra(HARGA_LAPANGAN, hargaLapanganStr);
                intent.putExtra(GAMBAR_LAPANGAN, gambarLapanganStr);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lapanganData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaLapangan, kategoriLapangan, jenisLapangan, hargaSewa;
        Button editLapangan, hapusLapangan;
        ImageView gambarLapangan;
        RecyclerView jamSewaRecycler;
        RecyclerView.LayoutManager layoutManager;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            gambarLapangan = itemView.findViewById(R.id.lapangan_picture);
            namaLapangan = itemView.findViewById(R.id.lapangan_title);
            kategoriLapangan = itemView.findViewById(R.id.lapangan_type);
            jenisLapangan = itemView.findViewById(R.id.rumput_lapangan);
            hargaSewa = itemView.findViewById(R.id.lapangan_price);
            jamSewaRecycler = itemView.findViewById(R.id.jam_lapangan_recycler);
            jamSewaRecycler.setLayoutManager(layoutManager);
            editLapangan = itemView.findViewById(R.id.edit_lapangan_button);
            hapusLapangan = itemView.findViewById(R.id.hapus_lapangan_button);
        }
    }
}
