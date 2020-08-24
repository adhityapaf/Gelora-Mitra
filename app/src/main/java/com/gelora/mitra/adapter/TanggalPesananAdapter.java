package com.gelora.mitra.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.mitra.R;
import com.gelora.mitra.model.PesananData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TanggalPesananAdapter extends RecyclerView.Adapter<TanggalPesananAdapter.ViewHolder> {
    private static final String TAG = "TanggalPesananAdapter";
    ArrayList<String> tanggalPesanan;
    ArrayList<PesananData> pesananData;
    Context context;
    DatabaseReference ref;
    String tanggalPesanString;
    Locale locale = new Locale("id", "ID");


    public TanggalPesananAdapter(ArrayList<String> tanggalPesanan, Context context) {
        this.tanggalPesanan = tanggalPesanan;
        this.context = context;
    }

    @NonNull
    @Override
    public TanggalPesananAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_holder_tanggal_pesanan, parent, false);
        ref = FirebaseDatabase.getInstance().getReference("pesanan_pemilik").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return new TanggalPesananAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TanggalPesananAdapter.ViewHolder holder, int position) {
        DateFormat format = new SimpleDateFormat("EEEEEEE, dd MMMM yyyy", locale);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(tanggalPesanan.get(position)));
        tanggalPesanString = format.format(calendar.getTime());
        holder.tanggalPesanan.setText(tanggalPesanString);
        holder.pesananListRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        holder.pesananListRecycler.setLayoutManager(linearLayoutManager);
        ref.child(tanggalPesanan.get(position)).child("id_pesanan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pesananData = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        PesananData pesananData2 = ds.getValue(PesananData.class);
                        pesananData.add(pesananData2);
                    }
                    PesananAdapter pesananListAdapter = new PesananAdapter(pesananData, context);
                    holder.pesananListRecycler.setAdapter(pesananListAdapter);
                } else  {
                    Log.d(TAG, "TanggalPesanan List : Not Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(tanggalPesanan.get(position)).child("id_pesanan").keepSynced(true);
    }

    @Override
    public int getItemCount() {
        return tanggalPesanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tanggalPesanan;
        RecyclerView pesananListRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggalPesanan = itemView.findViewById(R.id.tanggal_transaksi);
            pesananListRecycler = itemView.findViewById(R.id.orderListRecycler);
        }
    }
}
