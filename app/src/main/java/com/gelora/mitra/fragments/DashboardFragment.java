package com.gelora.mitra.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gelora.mitra.R;
import com.gelora.mitra.dialogs.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardFragment extends Fragment {

    TextView namaMitra, penghasilanText, lapanganText, pesananText;
    FirebaseAuth firebaseAuth;
    LoadingDialog loadingDialog1;
    String totalPesanan, totalPenghasilan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        namaMitra = v.findViewById(R.id.namaMitraLabel);
        penghasilanText = v.findViewById(R.id.penghasilan_amount);
        lapanganText = v.findViewById(R.id.lapangan_amount);
        pesananText = v.findViewById(R.id.totalPesanan_amount);
        namaMitra.setText("");
        penghasilanText.setText("");
        pesananText.setText("");
        lapanganText.setText("");
        loadingDialog1 = new LoadingDialog(getActivity());
        loadingDialog1.startLoadingDialog();
        loadUserInformation();
        return v;
    }

    private void loadUserInformation(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        DatabaseReference naamRef = databaseReference.child("nama");
        if (user!=null){
            if (naamRef.getKey() != null){
                naamRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        namaMitra.setText(snapshot.getValue().toString());
                        loadingDialog1.dissmissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                namaMitra.setText("Gagal Fetch");
            }
        }
        final DatabaseReference totalLapanganRef =  FirebaseDatabase.getInstance().getReference("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("total_lapangan");
        totalLapanganRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    lapanganText.setText(snapshot.getValue().toString());

                } else {
                    lapanganText.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        totalLapanganRef.keepSynced(true);
        final DatabaseReference totalPesananRef = FirebaseDatabase.getInstance().getReference("pesanan_pemilik").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("total_pesanan");
        totalPesananRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    totalPesanan = snapshot.getValue().toString();
                    pesananText.setText(totalPesanan);
                } else {
                    totalPesananRef.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference totalPenghasilanRef = FirebaseDatabase.getInstance().getReference("pesanan_pemilik").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("total_penghasilan");
        totalPenghasilanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    totalPenghasilan = snapshot.getValue().toString();
                    penghasilanText.setText(totalPenghasilan);
                } else {
                    totalPenghasilanRef.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
