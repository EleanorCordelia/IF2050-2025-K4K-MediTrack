package com.meditrack.model;

public class Pengguna {
    private int idPengguna;
    private String nama;
    private String email;
    private String password;
    private String tanggalLahir;   // format 'YYYY-MM-DD'
    private String jenisKelamin;   // 'Pria','Wanita','Lainnya'
    private Double tinggiBadan;    // nullable
    private Double beratBadan;     // nullable

    public Pengguna() { }

    public Pengguna(int idPengguna, String nama, String email, String password,
                    String tanggalLahir, String jenisKelamin,
                    Double tinggiBadan, Double beratBadan) {
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.tinggiBadan = tinggiBadan;
        this.beratBadan = beratBadan;
    }

    public int getIdPengguna() {
        return idPengguna;
    }
    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }
    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public Double getTinggiBadan() {
        return tinggiBadan;
    }
    public void setTinggiBadan(Double tinggiBadan) {
        this.tinggiBadan = tinggiBadan;
    }

    public Double getBeratBadan() {
        return beratBadan;
    }
    public void setBeratBadan(Double beratBadan) {
        this.beratBadan = beratBadan;
    }
}
