package com.gelora.mitra.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.mitra.R;
import com.gelora.mitra.adapter.PesananAdapter;
import com.gelora.mitra.adapter.TanggalPesananAdapter;
import com.gelora.mitra.model.PesananData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PesananFragment extends Fragment {

    EditText searchBox;
    RecyclerView pesananRecycler;
    ProgressBar progressBar;
    Context mContext;
    DatabaseReference pesananRef;
    ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesanan, container, false);
        searchBox = view.findViewById(R.id.searchbox_pesanan);
        pesananRecycler = view.findViewById(R.id.pesanan_recycler);
        progressBar = view.findViewById(R.id.progressbarlingkaran);
        mContext = getContext();
        pesananRef = FirebaseDatabase.getInstance().getReference("pesanan_pemilik").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        pesananRecycler.setHasFixedSize(true);
        pesananRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        readData();
        return view;
    }

    private void readData() {
        pesananRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        list.add(ds.getKey());
                    }
                    TanggalPesananAdapter tanggalPesananAdapter = new TanggalPesananAdapter(list, mContext);
                    pesananRecycler.setAdapter(tanggalPesananAdapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mContext, "Daftar Pesanan Anda Masih Kosong.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
