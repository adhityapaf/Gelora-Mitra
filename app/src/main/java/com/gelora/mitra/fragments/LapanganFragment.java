package com.gelora.mitra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.mitra.R;
import com.gelora.mitra.activity.TambahkanLapangan;
import com.gelora.mitra.adapter.LapanganAdapter;
import com.gelora.mitra.model.LapanganData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LapanganFragment extends Fragment {
    ArrayList<LapanganData> list;
    ArrayList<String> jamArray;
    FloatingActionButton floatingActionButton;
    DatabaseReference ref;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lapangan, container, false);
        ref = FirebaseDatabase.getInstance().getReference("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView = view.findViewById(R.id.lapangan_recycler);
        floatingActionButton = view.findViewById(R.id.tambahLapangan_fab);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        readData();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahkanLapangan.class);
                startActivity(intent);
            }
        });
//        ListAdapter listAdapter = new ListAdapter();
//        recyclerView.setAdapter(listAdapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void readData(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list = new ArrayList<>();
                    jamArray = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.child("id_lapangan").getChildren()){
                        LapanganData lp = ds.getValue(LapanganData.class);
                        list.add(lp);
                    }
                    System.out.println(list);
                    LapanganAdapter lapanganAdapter = new LapanganAdapter(list, getActivity());
                    recyclerView.setAdapter(lapanganAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
