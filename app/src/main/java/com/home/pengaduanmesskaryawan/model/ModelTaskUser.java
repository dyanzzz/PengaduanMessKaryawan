package com.home.pengaduanmesskaryawan.model;

public class ModelTaskUser {

    private String nomor, kdUser, username, nama, kdKamar;

    public ModelTaskUser() {
    }

    public ModelTaskUser(String nomor, String kdUser, String username, String nama, String kdKamar) {
        this.nomor      = nomor;
        this.kdUser     = kdUser;
        this.username   = username;
        this.nama       = nama;
        this.kdKamar       = kdKamar;
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

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKdKamar() {
        return kdKamar;
    }
    public void setKdKamar(String kdKamar) {
        this.kdKamar = kdKamar;
    }

}
