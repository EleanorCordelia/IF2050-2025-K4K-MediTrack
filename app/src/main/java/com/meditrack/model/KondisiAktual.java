package com.meditrack.model;

public class KondisiAktual {
    private int idKondisi;
    private int idPengguna;
    private String tekananDarah;   // format 'SYS/DIA', misalnya '120/80'
    private int detakJantung;      // dalam bpm (beats per minute)
    private double suhuTubuh;      // dalam derajat Celsius, misalnya 36.7
    private String waktuPencatatan; // format 'YYYY-MM-DD HH:MM:SS'

    // Konstruktor kosong (dibutuhkan misalnya untuk inisialisasi manual atau framework)
    public KondisiAktual() { }

    // Konstruktor lengkap (digunakan misalnya pada pemetaan ResultSet ke objek)
    public KondisiAktual(int idKondisi, int idPengguna, String tekananDarah, int detakJantung,
                         double suhuTubuh, String waktuPencatatan) {
        this.idKondisi = idKondisi;
        this.idPengguna = idPengguna;
        this.tekananDarah = tekananDarah;
        this.detakJantung = detakJantung;
        this.suhuTubuh = suhuTubuh;
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

    public String getWaktuPencatatan() {
        return waktuPencatatan;
    }

    public void setWaktuPencatatan(String waktuPencatatan) {
        this.waktuPencatatan = waktuPencatatan;
    }
}