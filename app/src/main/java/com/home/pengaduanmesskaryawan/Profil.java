package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profil extends AppCompatActivity {

    String strKdUser, strKdKamar, strLevel;
    EditText blokKamar, noKamar, username, nama, tanggalLahir, jenisKelamin, alamat;
    TextView menunggu, proses, selesai;
    LinearLayout layoutStatusKeluhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent   = getIntent();
        strKdUser       = intent.getStringExtra(Config.DISP_KD_USER);
        strKdKamar      = intent.getStringExtra(Config.DISP_KD_KAMAR);
        strLevel        = intent.getStringExtra(Config.DISP_LEVEL_USER);

        blokKamar       = findViewById(R.id.blokKamar);
        noKamar         = findViewById(R.id.noKamar);
        username        = findViewById(R.id.username);
        nama            = findViewById(R.id.nama);
        tanggalLahir    = findViewById(R.id.tanggalLahir);
        jenisKelamin    = findViewById(R.id.jenisKelamin);
        alamat          = findViewById(R.id.alamat);
        menunggu        = findViewById(R.id.menunggu);
        proses          = findViewById(R.id.proses);
        selesai         = findViewById(R.id.selesai);
        layoutStatusKeluhan         = findViewById(R.id.layoutStatusKeluhan);
        if(strLevel.equals("1")) {
            layoutStatusKeluhan.setVisibility(View.GONE);
        }

        if(Config.CEK_KONEKSI(Profil.this)) {
            getJSON();
        } else {
            Toast.makeText(Profil.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void getJSON(){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profil.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(DaftarMasjid.this, s, Toast.LENGTH_SHORT).show();
                showData(s);
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_USER, strKdUser);
                params.put(Config.DISP_KD_KAMAR, strKdKamar);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_PROFIL, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showData(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo               = result.getJSONObject(0);
            String disp_blok_kamar      = jo.getString(Config.DISP_BLOK_KAMAR);
            String disp_no_kamar        = jo.getString(Config.DISP_NO_KAMAR);
            String disp_nama            = jo.getString(Config.DISP_NAMA);
            String disp_username        = jo.getString(Config.DISP_USERNAME);
            String disp_tanggal_lahir   = jo.getString(Config.DISP_TANGGAL_LAHIR);
            String disp_jenis_kelamin   = jo.getString(Config.DISP_JENIS_KELAMIN);
            String disp_alamat          = jo.getString(Config.DISP_ALAMAT);
            String disp_kd_user         = jo.getString(Config.DISP_KD_USER);
            String disp_kd_kamar        = jo.getString(Config.DISP_KD_KAMAR);

            String disp_jumlah_keluhan_menunggu = jo.getString(Config.DISP_JUMLAH_KELUHAN_MENUNGGU);
            String disp_jumlah_keluhan_proses   = jo.getString(Config.DISP_JUMLAH_KELUHAN_PROSES);
            String disp_jumlah_keluhan_selesai  = jo.getString(Config.DISP_JUMLAH_KELUHAN_SELESAI);

            blokKamar.setText(disp_blok_kamar);
            noKamar.setText(disp_no_kamar);
            username.setText(disp_username);
            nama.setText(disp_nama);
            tanggalLahir.setText(disp_tanggal_lahir);
            jenisKelamin.setText(disp_jenis_kelamin);
            alamat.setText(disp_alamat);

            menunggu.setText(disp_jumlah_keluhan_menunggu);
            proses.setText(disp_jumlah_keluhan_proses);
            selesai.setText(disp_jumlah_keluhan_selesai);

            strKdUser   = disp_kd_user;
            strKdKamar  = disp_kd_kamar;

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(Profil.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.edit:
                Intent intent = new Intent(Profil.this, ProfilEdit.class);
                intent.putExtra(Config.DISP_KD_USER, strKdUser);
                intent.putExtra(Config.DISP_KD_KAMAR, strKdKamar);
                startActivity(intent);
                //Toast.makeText(Profil.this, "tes tombol", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        //if(strLevel.equals("2")) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        //}
        return true;
    }
}
