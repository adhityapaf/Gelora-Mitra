package com.gelora.mitra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gelora.mitra.R;
import com.gelora.mitra.activity.MainActivity;
import com.gelora.mitra.activity.SplashScreen;
import com.gelora.mitra.activity.TambahkanLapangan;
import com.google.firebase.auth.FirebaseAuth;

public class AkunFragment extends Fragment {
    Button logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akun, container, false);
        logout = view.findViewById(R.id.logout_button);
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
}
