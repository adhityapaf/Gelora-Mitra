package com.gelora.mitra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AkunFragment extends Fragment {
    Button logout, simpanPerubahan, tentangKami;
    EditText namaMitra, emailMitra, passwordMitra;
    DatabaseReference userRef;
    String nama, email, passwrd;

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
        loadProfileUser();
        logout = view.findViewById(R.id.logout_button);
        simpanPerubahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                userRef.child("nama").setValue(nama);
                userRef.child("password").setValue(passwrd);
                FirebaseAuth.getInstance().getCurrentUser().updatePassword(passwrd);
            Toast.makeText(getContext(), "Berhasil simpan perubahan akun", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
