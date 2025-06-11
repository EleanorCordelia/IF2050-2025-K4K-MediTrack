package com.meditrack.model;

public class Wearable {
    private int idWearable;
    private String tipe;
    private String versiFirmware;
    private int idPengguna;
    private boolean statusSinkron;

    // Konstruktor kosong (dibutuhkan, misalnya untuk inisialisasi manual atau framework)
    public Wearable() { }

    // Konstruktor lengkap (misalnya untuk memetakan hasil ResultSet ke objek Wearable)
    public Wearable(int idWearable, String tipe, String versiFirmware, int idPengguna, boolean statusSinkron) {
        this.idWearable = idWearable;
        this.tipe = tipe;
        this.versiFirmware = versiFirmware;
        this.idPengguna = idPengguna;
        this.statusSinkron = statusSinkron;
    }

    // Getter & Setter

    public int getIdWearable() {
        return idWearable;
    }

    public void setIdWearable(int idWearable) {
        this.idWearable = idWearable;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getVersiFirmware() {
        return versiFirmware;
    }

    public void setVersiFirmware(String versiFirmware) {
        this.versiFirmware = versiFirmware;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public boolean isStatusSinkron() {
        return statusSinkron;
    }

    public void setStatusSinkron(boolean statusSinkron) {
        this.statusSinkron = statusSinkron;
    }
}
