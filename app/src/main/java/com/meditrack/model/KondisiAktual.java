package com.meditrack.model;

import java.time.LocalDateTime;

public class KondisiAktual {
    private Integer id;
    private Integer userId;
    private String tekananDarah;
    private Integer detakJantung;
    private Double suhuTubuh;
    private String tingkatStres;
    private Double durasiTidur;
    private Integer durasiOlahraga;
    private Integer jumlahLangkah;
    private LocalDateTime tglPencatatan;

    // 1) No-arg constructor (dibutuhkan oleh DAO)
    public KondisiAktual() {
    }

    // 2) Full-arg constructor (bila perlu)
    public KondisiAktual(Integer id, Integer userId, String tekananDarah,
                         Integer detakJantung, Double suhuTubuh,
                         String tingkatStres, Double durasiTidur,
                         Integer durasiOlahraga, Integer jumlahLangkah,
                         LocalDateTime tglPencatatan) {
        this.id = id;
        this.userId = userId;
        this.tekananDarah = tekananDarah;
        this.detakJantung = detakJantung;
        this.suhuTubuh = suhuTubuh;
        this.tingkatStres = tingkatStres;
        this.durasiTidur = durasiTidur;
        this.durasiOlahraga = durasiOlahraga;
        this.jumlahLangkah = jumlahLangkah;
        this.tglPencatatan = tglPencatatan;
    }

    // 3) Constructor untuk insert baru (tanpa id & timestamp)
    public KondisiAktual(Integer userId, String tekananDarah,
                         Integer detakJantung, Double suhuTubuh,
                         String tingkatStres, Double durasiTidur,
                         Integer durasiOlahraga, Integer jumlahLangkah) {
        this(null, userId, tekananDarah, detakJantung, suhuTubuh,
                tingkatStres, durasiTidur, durasiOlahraga, jumlahLangkah,
                null);
    }

    // getters...
    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public String getTekananDarah() { return tekananDarah; }
    public Integer getDetakJantung() { return detakJantung; }
    public Double getSuhuTubuh() { return suhuTubuh; }
    public String getTingkatStres() { return tingkatStres; }
    public Double getDurasiTidur() { return durasiTidur; }
    public Integer getDurasiOlahraga() { return durasiOlahraga; }
    public Integer getJumlahLangkah() { return jumlahLangkah; }
    public LocalDateTime getTglPencatatan() { return tglPencatatan; }

    // setters...
    public void setId(Integer id) { this.id = id; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public void setTekananDarah(String tekananDarah) { this.tekananDarah = tekananDarah; }
    public void setDetakJantung(Integer detakJantung) { this.detakJantung = detakJantung; }
    public void setSuhuTubuh(Double suhuTubuh) { this.suhuTubuh = suhuTubuh; }
    public void setTingkatStres(String tingkatStres) { this.tingkatStres = tingkatStres; }
    public void setDurasiTidur(Double durasiTidur) { this.durasiTidur = durasiTidur; }
    public void setDurasiOlahraga(Integer durasiOlahraga) { this.durasiOlahraga = durasiOlahraga; }
    public void setJumlahLangkah(Integer jumlahLangkah) { this.jumlahLangkah = jumlahLangkah; }
    public void setTglPencatatan(LocalDateTime tglPencatatan) { this.tglPencatatan = tglPencatatan; }
}