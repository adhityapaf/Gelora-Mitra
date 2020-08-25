package com.gelora.mitra.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gelora.mitra.R;
import com.gelora.mitra.activity.EditLapanganActivity;
import com.gelora.mitra.activity.TambahkanLapangan;
import com.gelora.mitra.adapter.JamSewaAdapter;
import com.gelora.mitra.adapter.LapanganAdapter;
import com.gelora.mitra.model.LapanganData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.gelora.mitra.adapter.LapanganAdapter.GAMBAR_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.HARGA_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.ID_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.JENIS_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.KATEGORI_LAPANGAN;
import static com.gelora.mitra.adapter.LapanganAdapter.NAMA_LAPANGAN;

public class LapanganFragment extends Fragment {
    ArrayList<LapanganData> list;
    ArrayList<String> jamArray;
    FloatingActionButton floatingActionButton;
    DatabaseReference ref;
    RecyclerView recyclerView;
    EditText searchbox;
    FirebaseRecyclerOptions<LapanganData> options;
    FirebaseRecyclerAdapter<LapanganData, LapanganAdapter.ViewHolder> adapter;
    Context mContext;
    DatabaseReference lapanganRef, pemilikRef, totalLapanganCounter;
    HashMap<String, String> hashMap = new HashMap<String,String>();
    ArrayList<String> jamSewaArrayList;
    int pemilikCounter = 0;
    private static final String TAG = "LapanganFragment";
    TextView lapanganKosong;
    ImageView lapanganKosongGambar;
    @Override
    public void onStart() {
        readData();
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lapangan, container, false);
        lapanganKosong = view.findViewById(R.id.text_lapanganKosong);
        lapanganKosongGambar = view.findViewById(R.id.img_tambahkanLapanganKosong);
        mContext = getActivity().getApplicationContext();
        ref = FirebaseDatabase.getInstance().getReference("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        lapanganRef = FirebaseDatabase.getInstance().getReference("lapangan");
        pemilikRef = FirebaseDatabase.getInstance().getReference("pemilik_lapangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        totalLapanganCounter = pemilikRef.child("total_lapangan");
        totalLapanganCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pemilikCounter = Integer.parseInt(snapshot.getValue().toString());

                } else {
                    pemilikCounter = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView = view.findViewById(R.id.lapangan_recycler);
        floatingActionButton = view.findViewById(R.id.tambahLapangan_fab);
        searchbox = view.findViewById(R.id.searchbox_lapangan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        readData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        floatingActionButton.show();
                    }
                }, 1000);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0){
                    floatingActionButton.hide();
                } else if (dy > 0){
                    floatingActionButton.hide();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahkanLapangan.class);
                startActivity(intent);
            }
        });

        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                } else {
                    onStart();
                }
            }
        });
        return view;
    }

    private void setAdapter(String toString) {
        String upper = toString.toUpperCase();
        DatabaseReference queRef = ref.child("id_lapangan");
        Query query = queRef.orderByChild("searchString").startAt(upper).endAt(upper +"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<LapanganData>().setQuery(query, LapanganData.class).build();
        adapter = new FirebaseRecyclerAdapter<LapanganData, LapanganAdapter.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LapanganAdapter.ViewHolder holder,  int position, @NonNull final LapanganData model) {
                Glide.with(mContext)
                        .load(model.getGambar_lapangan())
                        .centerCrop()
                        .placeholder(R.drawable.background_fragments)
                        .into(holder.gambarLapangan);
                holder.namaLapangan.setText(model.getNama_lapangan());
                holder.kategoriLapangan.setText(model.getKategori_lapangan());
                holder.jenisLapangan.setText(model.getJenis_lapangan());
                String bilanganHarga = String.valueOf(model.getHarga());
                holder.hargaSewa.setText("Rp. "+bilanganHarga);
                hashMap = model.getJam_sewa();
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
                        id_lapanganStr = model.getId_lapangan();
                        ArrayList<String> jamList;
                        jamList = jamSewaArrayList;
                        hargaLapanganStr = holder.hargaSewa.getText().toString();
                        gambarLapanganStr = model.getGambar_lapangan();
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
                                String idLapangan = model.getId_lapangan();
                                FirebaseStorage photoStorage = FirebaseStorage.getInstance().getReference("foto_lapangan/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/id_lapangan/"+idLapangan).getStorage();
                                StorageReference photoRef =  photoStorage.getReferenceFromUrl(model.getGambar_lapangan());
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
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public LapanganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_lapangan, parent, false);
                return new LapanganAdapter.ViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public void readData(){
        searchbox.clearFocus();
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
                    lapanganKosong.setVisibility(View.GONE);
                    lapanganKosongGambar.setVisibility(View.GONE);
                    if (list.size() == 0){
                        lapanganKosong.setVisibility(View.VISIBLE);
                        lapanganKosongGambar.setVisibility(View.VISIBLE);
                    }
                } else {
                       lapanganKosong.setVisibility(View.VISIBLE);
                       lapanganKosongGambar.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
