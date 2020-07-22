package com.gelora.mitra.model;

public class PesananData {
    private String id_pesanan;
    private String nama_pemesan;
    private int total_harga;
    private String bukti_pembayaran;
    private String jam_pesan;
    private String tanggal_pesan;
    private String status_pesanan;

    public PesananData(String id_pesanan, String nama_pemesan, int total_harga, String bukti_pembayaran, String jam_pesan, String tanggal_pesan, String status_pesanan) {
        this.id_pesanan = id_pesanan;
        this.nama_pemesan = nama_pemesan;
        this.total_harga = total_harga;
        this.bukti_pembayaran = bukti_pembayaran;
        this.jam_pesan = jam_pesan;
        this.tanggal_pesan = tanggal_pesan;
        this.status_pesanan = status_pesanan;
    }

    public PesananData() {
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
