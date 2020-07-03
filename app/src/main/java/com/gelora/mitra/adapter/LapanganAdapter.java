package com.gelora.mitra.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gelora.mitra.R;
import com.gelora.mitra.activity.EditLapanganActivity;
import com.gelora.mitra.model.LapanganData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {

    ArrayList<LapanganData> lapanganData;
    Context mContext;
    DatabaseReference lapanganRef, pemilikRef, totalLapanganCounter;
    HashMap<String, String> hashMap = new HashMap<String,String>();
    ArrayList<String> jamSewaArrayList;
    int pemilikCounter = 0;
    private static final String TAG = "LapanganAdapter";
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
        lapanganRef = FirebaseDatabase.getInstance().getReference("lapangan");
        pemilikRef = FirebaseDatabase.getInstance().getReference("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        totalLapanganCounter = pemilikRef.child("total_lapangan");
        totalLapanganCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pemilikCounter = Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        holder.hapusLapangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Hapus Lapangan");
                builder.setMessage("Apakah Anda yakin menghapus lapangan " + holder.namaLapangan.getText() + " ?");
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idLapangan = lapanganData.get(position).getId_lapangan();
                        FirebaseStorage photoStorage = FirebaseStorage.getInstance().getReference("foto_lapangan/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/id_lapangan/"+idLapangan).getStorage();
                        StorageReference photoRef =  photoStorage.getReferenceFromUrl(lapanganData.get(position).getGambar_lapangan());
                        pemilikCounter--;
                        lapanganRef.child("id_lapangan").child(idLapangan).removeValue();
                        pemilikRef.child("id_lapangan").child(idLapangan).removeValue();
                        totalLapanganCounter.setValue(pemilikCounter);
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: Foto lapangan dihapus");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Foto lapangan gagal dihapus!");
                            }
                        });
                        Toast.makeText(mContext, "Lapangan "+holder.namaLapangan.getText()+" berhasil dihapus!", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);
                    }
                });
                builder.show();
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
