package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.DatePickerFragment;
import com.home.pengaduanmesskaryawan.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class InputKamar extends AppCompatActivity {

    String kdKamar, kdUser;
    private TextView titleForm;
    EditText blokKamar, noKamar, username, password, nama, tanggalLahir, alamat, jenisKelamin;
    Button btnSave, btnCancel;
    Switch showHidePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kamar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent   = getIntent();
        kdUser          = intent.getStringExtra(Config.DISP_KD_USER);
        kdKamar         = intent.getStringExtra(Config.DISP_KD_KAMAR);

        blokKamar       = findViewById(R.id.blokKamar);
        noKamar         = findViewById(R.id.noKamar);
        username        = findViewById(R.id.username);
        password        = findViewById(R.id.password);
        nama            = findViewById(R.id.nama);
        tanggalLahir    = findViewById(R.id.tanggalLahir);
        jenisKelamin    = findViewById(R.id.jenisKelamin);
        alamat          = findViewById(R.id.alamat);

        titleForm       = findViewById(R.id.titleForm);
        btnSave         = findViewById(R.id.btnSave);
        btnCancel       = findViewById(R.id.btnCancel);


        showHidePassword    = findViewById(R.id.showHidePassword);
        showHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                password.setSelection(password.getText().length());
            }
        });



        if(Config.CEK_KONEKSI(InputKamar.this)) {

            if(!kdKamar.equals("0")){
                blokKamar.setKeyListener(null);
                noKamar.setKeyListener(null);
                username.setKeyListener(null);
                getJSON();
            }
        } else {
            Toast.makeText(InputKamar.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
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
                loading = ProgressDialog.show(InputKamar.this, "", Config.ALERT_PLEASE_WAIT, false, false);
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
                params.put(Config.DISP_KD_USER, kdUser);
                params.put(Config.DISP_KD_KAMAR, kdKamar);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_KAMAR, params);
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
            String disp_user            = jo.getString(Config.DISP_USERNAME);
            String disp_password        = jo.getString(Config.DISP_PASSWORD);
            String disp_tanggal_lahir   = jo.getString(Config.DISP_TANGGAL_LAHIR);
            String disp_jenis_kelamin   = jo.getString(Config.DISP_JENIS_KELAMIN);
            String disp_alamat          = jo.getString(Config.DISP_ALAMAT);

            blokKamar.setText(disp_blok_kamar);
            noKamar.setText(disp_no_kamar);
            nama.setText(disp_nama);
            username.setText(disp_user);
            password.setText(disp_password);
            tanggalLahir.setText(disp_tanggal_lahir);
            jenisKelamin.setText(disp_jenis_kelamin);
            alamat.setText(disp_alamat);
            titleForm.setText("Edit Data Kamar");

        } catch (JSONException e){
            e.printStackTrace();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Toast.makeText(InputKamar.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
        }

    }


    private void saveKamar(View v){
        final String data_blokKamar     = blokKamar.getText().toString().trim();
        final String data_noKamar       = noKamar.getText().toString().trim();
        final String data_username      = username.getText().toString().trim();
        final String data_password      = password.getText().toString().trim();
        final String data_nama          = nama.getText().toString().trim();
        final String data_tanggalLahir  = tanggalLahir.getText().toString().trim();
        final String data_jenisKelamin  = jenisKelamin.getText().toString().trim();
        final String data_alamat        = alamat.getText().toString().trim();

        if(data_blokKamar.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_BLOK_KAMAR, Toast.LENGTH_SHORT).show();
            blokKamar.requestFocus();

        }else if(data_noKamar.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_NO_KAMAR, Toast.LENGTH_SHORT).show();
            noKamar.requestFocus();

        } else if(data_username.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_USERNAME, Toast.LENGTH_SHORT).show();
            username.requestFocus();

        } else if(data_password.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_DATA_PASSWORD, Toast.LENGTH_SHORT).show();
            password.requestFocus();

        } else if(data_nama.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_NAMA, Toast.LENGTH_SHORT).show();
            nama.requestFocus();

        } else if(data_tanggalLahir.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_TANGGAL_LAHIR, Toast.LENGTH_SHORT).show();
            tanggalLahir.requestFocus();

        } else if(data_alamat.equals("")) {
            Toast.makeText(InputKamar.this, Config.ALERT_ALAMAT, Toast.LENGTH_SHORT).show();
            alamat.requestFocus();

        } else {
            @SuppressLint("StaticFieldLeak")
            class saveKamar extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(InputKamar.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(InputKamar.this, s, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();

                    //from session
                    hashMap.put(Config.DISP_BLOK_KAMAR, data_blokKamar);
                    hashMap.put(Config.DISP_NO_KAMAR, data_noKamar);
                    hashMap.put(Config.DISP_USERNAME, data_username);
                    hashMap.put(Config.DISP_PASSWORD, data_password);
                    hashMap.put(Config.DISP_NAMA, data_nama);
                    hashMap.put(Config.DISP_TANGGAL_LAHIR, data_tanggalLahir);
                    hashMap.put(Config.DISP_JENIS_KELAMIN, data_jenisKelamin);
                    hashMap.put(Config.DISP_ALAMAT, data_alamat);
                    hashMap.put(Config.DISP_KD_TOMBOL, kdKamar);
                    hashMap.put(Config.DISP_KD_USER, kdUser);

                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendPostRequest(Config.URL_ACTION_KAMAR, hashMap);
                    return s;
                }
            }

            saveKamar usa = new saveKamar();
            usa.execute();

        }
    }


    public void onClick(View v){
        if(Config.CEK_KONEKSI(InputKamar.this)) {
            if (v == btnSave) {
                saveKamar(v);
            }else if (v == btnCancel) {
                onBackPressed();
            }else if(v == tanggalLahir){
                showDatePicker();
            }
        }else{
            Toast.makeText(InputKamar.this, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Toast.LENGTH_SHORT).show();
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

    public void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "DatePicker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int bulan           = month+1;
            String day_string   = String.valueOf(day);
            String month_string = String.valueOf(bulan);

            if(day < 10){
                day_string = "0" + String.valueOf(day);
            }
            if(bulan < 10){
                month_string = "0" + String.valueOf(bulan);
            }

            String stringOfDate = year + "-" + month_string + "-" + day_string;

            if(Config.CEK_KONEKSI(InputKamar.this)) {

                tanggalLahir.setText(stringOfDate);
            } else {
                Toast.makeText(InputKamar.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
