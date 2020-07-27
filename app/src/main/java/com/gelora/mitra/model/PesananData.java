package com.gelora.mitra.model;

public class PesananData {
    private String id_pesanan;
    private String nama_pemesan;
    private int total_harga;
    private String bukti_pembayaran;
    private String jam_pesan;
    private String tanggal_pesan;
    private String status_pesanan;
    private String nama_lapangan;
    private String alasan_status;
    private String uid_mitra;
    private String uid_pengguna;

    public PesananData(String id_pesanan, String nama_pemesan, int total_harga, String bukti_pembayaran, String jam_pesan, String tanggal_pesan, String status_pesanan, String nama_lapangan, String alasan_status, String uid_mitra, String uid_pengguna) {
        this.id_pesanan = id_pesanan;
        this.nama_pemesan = nama_pemesan;
        this.total_harga = total_harga;
        this.bukti_pembayaran = bukti_pembayaran;
        this.jam_pesan = jam_pesan;
        this.tanggal_pesan = tanggal_pesan;
        this.status_pesanan = status_pesanan;
        this.nama_lapangan = nama_lapangan;
        this.alasan_status = alasan_status;
        this.uid_mitra = uid_mitra;
        this.uid_pengguna = uid_pengguna;
    }

    public String getUid_pengguna() {
        return uid_pengguna;
    }

    public void setUid_pengguna(String uid_pengguna) {
        this.uid_pengguna = uid_pengguna;
    }

    public String getUid_mitra() {
        return uid_mitra;
    }

    public void setUid_mitra(String uid_mitra) {
        this.uid_mitra = uid_mitra;
    }

    public PesananData() {
    }

    public String getAlasan_status() {
        return alasan_status;
    }

    public void setAlasan_status(String alasan_status) {
        this.alasan_status = alasan_status;
    }

    public String getNama_lapangan() {
        return nama_lapangan;
    }

    public void setNama_lapangan(String nama_lapangan) {
        this.nama_lapangan = nama_lapangan;
    }

    public String getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(String id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public String getNama_pemesan() {
        return nama_pemesan;
    }

    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }

    public String getBukti_pembayaran() {
        return bukti_pembayaran;
    }

    public void setBukti_pembayaran(String bukti_pembayaran) {
        this.bukti_pembayaran = bukti_pembayaran;
    }

    public String getJam_pesan() {
        return jam_pesan;
    }

    public void setJam_pesan(String jam_pesan) {
        this.jam_pesan = jam_pesan;
    }

    public String getTanggal_pesan() {
        return tanggal_pesan;
    }

    public void setTanggal_pesan(String tanggal_pesan) {
        this.tanggal_pesan = tanggal_pesan;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
}
