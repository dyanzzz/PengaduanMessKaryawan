package com.home.pengaduanmesskaryawan.model;

public class ModelTaskReportResult {
    private String nomor, kdUser, kdKamar, kdKeluhan, blokKamar, noKamar, nama, keluhan, tanggalKeluhan, statusKeluhan, image;

    public ModelTaskReportResult() {
    }

    public ModelTaskReportResult(String nomor, String kdUser, String kdKamar, String kdKeluhan, String blokKamar, String noKamar,
                              String nama, String keluhan, String tanggalKeluhan, String statusKeluhan, String image) {
        this.nomor          = nomor;
        this.kdUser         = kdUser;
        this.kdKamar        = kdKamar;
        this.kdKeluhan      = kdKeluhan;
        this.blokKamar      = blokKamar;
        this.noKamar        = noKamar;
        this.nama           = nama;
        this.keluhan        = keluhan;
        this.tanggalKeluhan = tanggalKeluhan;
        this.statusKeluhan  = statusKeluhan;
        this.image          = image;
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

    public String getKdKeluhan() {
        return kdKeluhan;
    }
    public void setKdKeluhan(String kdKeluhan) {
        this.kdKeluhan = kdKeluhan;
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

    public String getKeluhan() {
        return keluhan;
    }
    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getTanggalKeluhan() {
        return tanggalKeluhan;
    }
    public void setTanggalKeluhan(String tanggalKeluhan) {
        this.tanggalKeluhan = tanggalKeluhan;
    }

    public String getStatusKeluhan() {
        return statusKeluhan;
    }
    public void setStatusKeluhan(String statusKeluhan) {
        this.statusKeluhan = statusKeluhan;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
