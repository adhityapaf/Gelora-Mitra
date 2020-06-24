package com.gelora.mitra.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.mitra.R;
import com.gelora.mitra.fragments.JamDialog;

import java.util.ArrayList;

public class JamLapanganAdapter extends RecyclerView.Adapter<JamLapanganAdapter.ViewHolder> {
    private static final String TAG = "JamLapanganAdapter";

    private ArrayList<String> jamjam = new ArrayList<>();
    private Context context;

    public JamLapanganAdapter(ArrayList<String> jamjam, Context context) {
        this.jamjam = jamjam;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Created");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jam_lapangan_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.jam.setText(jamjam.get(position));
        holder.jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Hapus Jam Sewa");
                builder.setMessage("Apakah Anda yakin menghapus jam " + holder.jam.getText() + " ?");
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jamjam.remove(jamjam.get(position));
                        notifyItemRemoved(position);
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return jamjam.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jam;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            jam = itemView.findViewById(R.id.jam_lapangan_isi);
        }
    }
}
