package com.gelora.mitra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.mitra.R;

import java.util.ArrayList;


public class JamSewaAdapter extends RecyclerView.Adapter<JamSewaAdapter.ViewHolder> {

    ArrayList<String> list;
    Context mContext;

    public JamSewaAdapter(ArrayList<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public JamSewaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.jam_lapangan_items_card, parent, false);
        return new JamSewaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JamSewaAdapter.ViewHolder holder, int position) {
        holder.jamText.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jamText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jamText = itemView.findViewById(R.id.jam_lapangan_isi);
        }
    }
}
