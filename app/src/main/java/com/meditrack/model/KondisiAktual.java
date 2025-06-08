package com.meditrack.model;

public class KondisiAktual {
    private int idKondisi;
    private int idPengguna;
    private String tekananDarah;   // format 'SYS/DIA', misalnya '120/80'
    private int detakJantung;      // dalam bpm (beats per minute)
    private double suhuTubuh;      // dalam derajat Celsius, misalnya 36.7
    private String tingkatStres;   // Rendah, Sedang, Tinggi
    private double durasiOlahraga; // dalam menit
    private double jumlahLangkah;  // dalam langkah
    private String waktuPencatatan; // format 'YYYY-MM-DD HH:MM:SS'

    // Konstruktor kosong
    public KondisiAktual() { }

    // Konstruktor lengkap
    public KondisiAktual(int idKondisi, int idPengguna, String tekananDarah, int detakJantung,
                         double suhuTubuh, String tingkatStres, double durasiOlahraga,
                         double jumlahLangkah, String waktuPencatatan) {
        this.idKondisi = idKondisi;
        this.idPengguna = idPengguna;
        this.tekananDarah = tekananDarah;
        this.detakJantung = detakJantung;
        this.suhuTubuh = suhuTubuh;
        this.tingkatStres = tingkatStres;
        this.durasiOlahraga = durasiOlahraga;
        this.jumlahLangkah = jumlahLangkah;
        this.waktuPencatatan = waktuPencatatan;
    }

    // Getter & Setter

    public int getIdKondisi() {
        return idKondisi;
    }

    public void setIdKondisi(int idKondisi) {
        this.idKondisi = idKondisi;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getTekananDarah() {
        return tekananDarah;
    }

    public void setTekananDarah(String tekananDarah) {
        this.tekananDarah = tekananDarah;
    }

    public int getDetakJantung() {
        return detakJantung;
    }

    public void setDetakJantung(int detakJantung) {
        this.detakJantung = detakJantung;
    }

    public double getSuhuTubuh() {
        return suhuTubuh;
    }

    public void setSuhuTubuh(double suhuTubuh) {
        this.suhuTubuh = suhuTubuh;
    }

    public String getTingkatStres() {
        return tingkatStres;
    }

    public void setTingkatStres(String tingkatStres) {
        this.tingkatStres = tingkatStres;
    }

    public double getDurasiOlahraga() {
        return durasiOlahraga;
    }

    public void setDurasiOlahraga(double durasiOlahraga) {
        this.durasiOlahraga = durasiOlahraga;
    }

    public double getJumlahLangkah() {
        return jumlahLangkah;
    }

    public void setJumlahLangkah(double jumlahLangkah) {
        this.jumlahLangkah = jumlahLangkah;
    }

    public String getWaktuPencatatan() {
        return waktuPencatatan;
    }

    public void setWaktuPencatatan(String waktuPencatatan) {
        this.waktuPencatatan = waktuPencatatan;
    }
}
