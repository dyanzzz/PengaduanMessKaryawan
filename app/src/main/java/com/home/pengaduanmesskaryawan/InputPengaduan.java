package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InputPengaduan extends AppCompatActivity {

    String strKdKamar, strKdUser, strKdTombol;
    TextView titleForm;
    EditText blokKamar, noKamar, nama, keluhan, kdKamar, kdUser;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pengaduan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent   = getIntent();
        strKdUser       = intent.getStringExtra(Config.DISP_KD_USER);
        strKdKamar      = intent.getStringExtra(Config.DISP_KD_KAMAR);
        strKdTombol     = intent.getStringExtra(Config.DISP_KD_TOMBOL);

        blokKamar       = findViewById(R.id.blokKamar);
        noKamar         = findViewById(R.id.noKamar);
        nama            = findViewById(R.id.nama);
        kdKamar         = findViewById(R.id.kdKamar);
        kdUser          = findViewById(R.id.kdUser);
        keluhan         = findViewById(R.id.keluhan);

        titleForm       = findViewById(R.id.titleForm);
        btnSave         = findViewById(R.id.btnSave);
        btnCancel       = findViewById(R.id.btnCancel);

        if(Config.CEK_KONEKSI(InputPengaduan.this)) {
            getJSON();
        } else {
            Toast.makeText(InputPengaduan.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void getJSON(){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputPengaduan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
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
                params.put(Config.DISP_KD_TOMBOL, strKdTombol);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_KELUHAN, params);
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
            String disp_kd_user         = jo.getString(Config.DISP_KD_USER);
            String disp_kd_kamar        = jo.getString(Config.DISP_KD_KAMAR);

            blokKamar.setText(disp_blok_kamar);
            noKamar.setText(disp_no_kamar);
            nama.setText(disp_nama);
            kdUser.setText(disp_kd_user);
            kdKamar.setText(disp_kd_kamar);

        } catch (JSONException e){
            e.printStackTrace();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Toast.makeText(InputPengaduan.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void saveKeluhan(View v){
        final String data_kdKamar   = kdKamar.getText().toString().trim();
        final String data_kdUser    = kdUser.getText().toString().trim();
        final String data_keluhan   = keluhan.getText().toString().trim();

        if(data_keluhan.equals("")) {
            Toast.makeText(InputPengaduan.this, Config.ALERT_KELUHAN, Toast.LENGTH_SHORT).show();
            //Snackbar.make(v, Config.ALERT_KELUHAN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            keluhan.requestFocus();

        } else {
            @SuppressLint("StaticFieldLeak")
            class save extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(InputPengaduan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(InputPengaduan.this, s, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();

                    //from session
                    hashMap.put(Config.DISP_KD_KAMAR, data_kdKamar);
                    hashMap.put(Config.DISP_KD_USER, data_kdUser);
                    hashMap.put(Config.DISP_KELUHAN, data_keluhan);
                    hashMap.put(Config.DISP_KD_TOMBOL, "0");

                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendPostRequest(Config.URL_ACTION_KELUHAN, hashMap);
                    return s;
                }
            }

            save usa = new save();
            usa.execute();

        }
    }

    public void onClick(View v){
        if(Config.CEK_KONEKSI(InputPengaduan.this)) {
            if (v == btnSave) {
                saveKeluhan(v);
            }else if (v == btnCancel) {
                onBackPressed();
            }
        }else{
            Toast.makeText(InputPengaduan.this, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Toast.LENGTH_SHORT).show();
        }
    }

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
