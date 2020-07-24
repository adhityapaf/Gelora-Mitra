package com.gelora.mitra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.mitra.R;
import com.gelora.mitra.model.PesananData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {

    ArrayList<PesananData> pesananData;
    Context mContext;
    Locale locale = new Locale("id", "ID");
    NumberFormat n = NumberFormat.getCurrencyInstance(locale);

    public PesananAdapter(ArrayList<PesananData> pesananData, Context mContext) {
        this.pesananData = pesananData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PesananAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_holder_pesanan, parent, false);
        return new PesananAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesananAdapter.ViewHolder holder, int position) {
        String s = n.format(pesananData.get(position).getTotal_harga());
        String a = s.replaceAll(",00", "").replaceAll("Rp", "Rp. ");
        holder.idTransaksi.setText(pesananData.get(position).getId_pesanan());
        holder.tanggalPesan.setText(pesananData.get(position).getTanggal_pesan());
        holder.waktuPesan.setText(pesananData.get(position).getJam_pesan());
        holder.namaPemesan.setText(pesananData.get(position).getNama_pemesan());
        holder.namaLapangan.setText(pesananData.get(position).getNama_lapangan());
        holder.totalHarga.setText(a);
        holder.lihatBuktiTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Bukti Tf", Toast.LENGTH_SHORT).show();
            }
        });
        holder.terimaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Terima btn", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tolakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Tolak nich", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pesananData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTransaksi, tanggalPesan, waktuPesan,  namaPemesan, namaLapangan, totalHarga, statusPesanan;
        ImageView statusIcon;
        Button lihatBuktiTransferButton, terimaButton, tolakButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTransaksi = itemView.findViewById(R.id.idTransaksi_field);
            tanggalPesan = itemView.findViewById(R.id.tanggalPesan_field);
            waktuPesan = itemView.findViewById(R.id.waktuPesan_field);
            namaPemesan = itemView.findViewById(R.id.namaPemesan_field);
            namaLapangan = itemView.findViewById(R.id.namaLapanganPesanan_field);
            totalHarga = itemView.findViewById(R.id.totalHarga_amount);
            statusPesanan = itemView.findViewById(R.id.statusPesananText);
            statusIcon = itemView.findViewById(R.id.statusIcon);
            lihatBuktiTransferButton = itemView.findViewById(R.id.lihatBuktiTransfer_button);
            terimaButton = itemView.findViewById(R.id.verifikasi_button);
            tolakButton = itemView.findViewById(R.id.tolak_button);
        }
    }
}
