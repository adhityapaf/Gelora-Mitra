package com.gelora.mitra.model;

import com.gelora.mitra.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LapanganData {
    private String nama_lapangan;
    private String gambar_lapangan;
    private long harga;
    private String kategori_lapangan;
    private String jenis_lapangan;
    private String UID_Mitra;
    HashMap<String,String> jam_sewa;

    public HashMap<String, String> getJam_sewa() {
        return jam_sewa;
    }

    public void setJam_sewa(HashMap<String, String> jam_sewa) {
        this.jam_sewa = jam_sewa;
    }

    public LapanganData(){

    }
    public String getNama_lapangan() {
        return nama_lapangan;
    }

    public void setNama_lapangan(String nama_lapangan) {
        this.nama_lapangan = nama_lapangan;
    }

    public String getGambar_lapangan() {
        return gambar_lapangan;
    }

    public void setGambar_lapangan(String gambar_lapangan) {
        this.gambar_lapangan = gambar_lapangan;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public String getKategori_lapangan() {
        return kategori_lapangan;
    }

    public void setKategori_lapangan(String kategori_lapangan) {
        this.kategori_lapangan = kategori_lapangan;
    }

    public String getJenis_lapangan() {
        return jenis_lapangan;
    }

    public void setJenis_lapangan(String jenis_lapangan) {
        this.jenis_lapangan = jenis_lapangan;
    }

    public String getUID_Mitra() {
        return UID_Mitra;
    }

    public void setUID_Mitra(String UID_Mitra) {
        this.UID_Mitra = UID_Mitra;
    }


    public LapanganData(String nama_lapangan, String gambar_lapangan, long harga, String kategori_lapangan, String jenis_lapangan, String UID_Mitra) {
        this.nama_lapangan = nama_lapangan;
        this.gambar_lapangan = gambar_lapangan;
        this.harga = harga;
        this.kategori_lapangan = kategori_lapangan;
        this.jenis_lapangan = jenis_lapangan;
        this.UID_Mitra = UID_Mitra;
    }
}
