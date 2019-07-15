package com.home.pengaduanmesskaryawan.model;

public class ModelTaskKamar {
    private String nomor, kdUser, kdKamar, username, blokKamar, noKamar, nama;

    public ModelTaskKamar() {
    }

    public ModelTaskKamar(String nomor, String kdUser, String kdKamar, String username, String blokKamar, String noKamar, String nama) {
        this.nomor      = nomor;
        this.kdUser     = kdUser;
        this.kdKamar    = kdKamar;
        this.username   = username;
        this.blokKamar  = blokKamar;
        this.noKamar    = noKamar;
        this.nama       = nama;
    }

    public String getNomor() {
        return nomor;
    }
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getKdUser() {
        return kdUser;
    }
    public void setKdUser(String kdUser) {
        this.kdUser = kdUser;
    }

    public String getKdKamar() {
        return kdKamar;
    }
    public void setKdKamar(String kdKamar) {
        this.kdKamar = kdKamar;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getBlokKamar() {
        return blokKamar;
    }
    public void setBlokKamar(String blokKamar) {
        this.blokKamar = blokKamar;
    }

    public String getNoKamar() {
        return noKamar;
    }
    public void setNoKamar(String noKamar) {
        this.noKamar = noKamar;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

}
