package com.gelora.mitra.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.gelora.mitra.activity.DetailPesananActivity;
import com.gelora.mitra.model.PesananData;

import java.util.ArrayList;

import static com.gelora.mitra.adapter.LapanganAdapter.NAMA_LAPANGAN;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {
    public static final String ID_PESANAN = "com.gelora.pengguna.id_pesanan";
    public static final String NAMA_PEMESAN = "com.gelora.pengguna.nama_pemesan";
    public static final String TOTAL_HARGA = "com.gelora.pengguna.total_harga";
    public static final String BUKTI_PEMBAYARAN = "com.gelora.pengguna.bukti_pembayaran";
    public static final String JAM_PESANAN = "com.gelora.pengguna.jam_pesanan";
    public static final String TANGGAL_PESANAN = "com.gelora.pengguna.tanggal_pesanan";
    public static final String STATUS_PESANAN = "com.gelora.pengguna.status_pesanan";
    public static final String ALASAN_PESANAN = "com.gelora.pengguna.alasan_pesanan";
    public static final String UID_MITRA = "com.gelora.pengguna.uid_mitra";
    public static final String UID_PELANGGAN = "com.gelora.pengguna.uid_mitra";
    public static final String TANGGAL_PESAN_USER = "com.gelora.pengguna.tanggal_pesan_user";



    ArrayList<PesananData> pesananData;
    Context mContext;

    public PesananAdapter(ArrayList<PesananData> pesananData, Context mContext) {
        this.pesananData = pesananData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PesananAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_holder_list_pesanan, parent, false);
        return new PesananAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PesananAdapter.ViewHolder holder, final int position) {
        holder.orderId.setText(pesananData.get(position).getId_pesanan());
        holder.orderStatus.setText(pesananData.get(position).getStatus_pesanan());
        holder.lihatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPesananActivity.class);
                intent.putExtra(ID_PESANAN, holder.orderId.getText().toString());
                intent.putExtra(NAMA_PEMESAN, pesananData.get(position).getNama_pemesan());
                intent.putExtra(TOTAL_HARGA, pesananData.get(position).getTotal_harga());
                intent.putExtra(BUKTI_PEMBAYARAN, pesananData.get(position).getBukti_pembayaran());
                intent.putExtra(JAM_PESANAN, pesananData.get(position).getJam_pesan());
                intent.putExtra(TANGGAL_PESANAN, pesananData.get(position).getTanggal_pesan());
                intent.putExtra(STATUS_PESANAN, pesananData.get(position).getStatus_pesanan());
                intent.putExtra(NAMA_LAPANGAN, pesananData.get(position).getNama_lapangan());
                intent.putExtra(ALASAN_PESANAN, pesananData.get(position).getAlasan_status());
                intent.putExtra(UID_MITRA, pesananData.get(position).getUid_mitra());
                intent.putExtra(UID_PELANGGAN, pesananData.get(position).getUid_pengguna());
                intent.putExtra(TANGGAL_PESAN_USER, pesananData.get(position).getTanggal_pesan_user());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pesananData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, lihatDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            lihatDetail = itemView.findViewById(R.id.lihatBukti);
        }
    }
}
