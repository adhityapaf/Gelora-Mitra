package com.gelora.mitra.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gelora.mitra.R;
import com.gelora.mitra.activity.MainActivity;
import com.gelora.mitra.activity.SplashScreen;
import com.gelora.mitra.activity.TambahkanLapangan;
import com.gelora.mitra.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AkunFragment extends Fragment {
    private static final String TAG = "AkunFragment";
    Button logout, simpanPerubahan, tentangKami;
    EditText namaMitra, emailMitra, passwordMitra;
    DatabaseReference userRef;
    String nama, email, passwrd;
    AuthCredential credential;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akun, container, false);
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        namaMitra = view.findViewById(R.id.namaMitraEdit_field);
        emailMitra = view.findViewById(R.id.emailMitraEdit_field);
        passwordMitra = view.findViewById(R.id.passwordMitraEdit_field);
        simpanPerubahan = view.findViewById(R.id.simpanPerubahan_button);
        tentangKami = view.findViewById(R.id.tentangKami_button);
        context = getContext();
        loadProfileUser();
        logout = view.findViewById(R.id.logout_button);
        simpanPerubahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanPerubahan.setBackground(context.getDrawable(R.drawable.disabled_green_stroke_rectangle));
                simpanPerubahan.setText("Menyimpan Perubahan....");
                nama = namaMitra.getText().toString();
                passwrd = passwordMitra.getText().toString();
                if (TextUtils.isEmpty(nama)) {
                    namaMitra.setError("Nama tidak boleh kosong.");
                    return;
                }
                if (TextUtils.isEmpty(passwrd)) {
                    passwordMitra.setError("Password tidak boleh kosong.");
                    return;
                }
                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential);
                FirebaseAuth.getInstance().getCurrentUser().updatePassword(passwrd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userRef.child("nama").setValue(nama);
                        userRef.child("password").setValue(passwrd);
                        simpanPerubahan.setBackground(context.getDrawable(R.drawable.button_green_rounded_5dp));
                        simpanPerubahan.setText("Simpan Perubahan");
                        Toast.makeText(context, "Berhasil simpan perubahan akun", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        simpanPerubahan.setBackground(context.getDrawable(R.drawable.button_green_rounded_5dp));
                        simpanPerubahan.setText("Simpan Perubahan");
                        Toast.makeText(context, "Gagal Mengubah Profil, Coba lagi.", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SplashScreen.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        tentangKami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TentangKami tentangKami = new TentangKami();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, tentangKami, "FindTetangKami")
                        .addToBackStack(null)
                        .commit();
                Log.d(TAG, "onClick: Success");
            }
        });
        return view;
    }

    void loadProfileUser() {
        userRef.child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.getValue().toString();
                emailMitra.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.child("nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama = snapshot.getValue().toString();
                namaMitra.setText(nama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passwrd = snapshot.getValue().toString();
                passwordMitra.setText(passwrd);
                credential = EmailAuthProvider.getCredential(email,passwrd);
                Log.d(TAG, "AuthCred: "+credential);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
