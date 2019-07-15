package com.home.pengaduanmesskaryawan.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

@SuppressLint("Registered")
public class Config extends AppCompatActivity {

    //JSON
    public static final String TAG_JSON_ARRAY   = "result";
    public static final int
            TAMPIL_ERROR = 1;

    //server
    //public static final String Server           = "http://192.168.43.125/pengaduan_keluhan/";
    //public static final String Server           = "https://pengaduankeluhan.000webhostapp.com/";
    public static final String Server           = "http://pengaduanmess.dx.am/";

    //url
    public static final String LOGIN_URL                    = Server + "login.php";
    public static final String URL_GET_KAMAR                = Server + "daftarKamar.php";
    public static final String URL_ACTION_KAMAR             = Server + "actionKamar.php";
    public static final String URL_GET_KELUHAN              = Server + "daftarKeluhan.php";
    public static final String URL_ACTION_KELUHAN           = Server + "actionKeluhan.php";
    public static final String URL_GET_PROFIL               = Server + "profil.php";


    public static final String KEY_USERNAME                 = "username";
    public static final String KEY_PASSWORD                 = "password";
    public static final String KEY_VERSI                    = "versi";
    public static final String VALUE_VERSI                  = "1.0.1";

    //validasi allert
    public static final String ALERT_USERNAME               = "* Please Input Username";
    public static final String ALERT_PASSWORD               = "* Please Input Password";
    public static final String ALERT_PASSWORD_LAMA          = "* Please Input Current Password";
    public static final String ALERT_PASSWORD_BARU          = "* Please Input New Password and must be more than 3 characters in length";
    public static final String ALERT_PASSWORD_BARU2         = "* Please Input Re-Type New Password and must be more than 3 characters in length";
    public static final String ALERT_PASSWORD_BARU_SAMA     = "* Password must match";
    public static final String ALERT_TITLE_CONN_ERROR       = "Connection Error";
    public static final String ALERT_MESSAGE_CONN_ERROR     = "Unable to connect with the server. Check your internet connection and try again.";
    public static final String ALERT_MESSAGE_NO_CONN        = "No connection";
    public static final String ALERT_MESSAGE_SRV_NOT_FOUND  = "Server not found";
    public static final String ALERT_OK_BUTTON              = "Ok";
    public static final String ALERT_NOT_FOUND              = "Data Not Found";
    public static final String ALERT_LOADING                = "Loading";
    public static final String ALERT_PLEASE_WAIT            = "Please Wait";

    public static final String ALERT_BLOK_KAMAR             = "* Mohon Input Blok Kamar";
    public static final String ALERT_NO_KAMAR               = "* Mohon Input No Kamar";
    public static final String ALERT_DATA_PASSWORD          = "* Mohon Input Password";
    public static final String ALERT_NAMA                   = "* Mohon Input Nama";
    public static final String ALERT_TANGGAL_LAHIR          = "* Mohon Input Tanggal Lahir";
    public static final String ALERT_ALAMAT                 = "* Mohon Input Alamat";
    public static final String ALERT_KELUHAN                = "* Mohon Input Keluhan";

    public static final String NOTIF_LOGOUT                 = "Are you sure want to Logout?";
    public static final String NOTIF_CONTACT_ADMIN          = "Please Contact Administrator.";
    public static final String HARUS_DIISI                  = "Form Input Cannot be Empty!!!";
    public static final String DISP_NOMOR                   = "nomor";

    //kamar

    public static final String DISP_KD_USER                 = "kdUsers";
    public static final String DISP_BLOK_KAMAR              = "blokKamar";
    public static final String DISP_KD_KAMAR                = "kdKamar";
    public static final String DISP_NO_KAMAR                = "noKamar";
    public static final String DISP_USERNAME                = "username";
    public static final String DISP_PASSWORD                = "password";
    public static final String DISP_NAMA                    = "nama";
    public static final String DISP_TANGGAL_LAHIR           = "tanggalLahir";
    public static final String DISP_JENIS_KELAMIN           = "jenisKelamin";
    public static final String DISP_ALAMAT                  = "alamat";
    public static final String DISP_KD_KELUHAN              = "kdKeluhan";
    public static final String DISP_KELUHAN                 = "keluhan";
    public static final String DISP_TANGGAL_KELUHAN         = "tanggalKeluhan";
    public static final String DISP_STATUS_KELUHAN          = "statusKeluhan";
    public static final String DISP_LEVEL_USER              = "levelUser";
    public static final String DISP_JUMLAH_KELUHAN_MENUNGGU = "menunggu";
    public static final String DISP_JUMLAH_KELUHAN_PROSES   = "proses";
    public static final String DISP_JUMLAH_KELUHAN_SELESAI  = "selesai";

    public static final String DISP_KD_TOMBOL               = "kdTombol";

    public static final String TITLE_DISP_WILAYAH           = "Form Input Wilayah";
    public static final String TITLE_DISP_MASJID            = "Form Input Masjid";
    public static final String TITLE_DISP_KAJIAN            = "Form Input Kajian";


    public static final boolean CEK_KONEKSI(Context cek){
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }

}
