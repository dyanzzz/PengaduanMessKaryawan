package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
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
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class InputPengaduan extends AppCompatActivity {

    String strKdKamar, strKdUser, strKdTombol;
    TextView titleForm;
    EditText blokKamar, noKamar, nama, keluhan, kdKamar, kdUser;
    Button btnSave, btnCancel, buttonChoose;

    ImageView imageView;
    Bitmap bitmap, decoded;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    String UPLOAD_URL = "http://10.0.2.2/android/upload_image/upload.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";
    String KEY_IMAGE = "image";
    String KEY_NAME = "name";
    String tag_json_obj = "json_obj_req";

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
        buttonChoose    = findViewById(R.id.buttonChoose);
        imageView       = findViewById(R.id.imageView);

        if(Config.CEK_KONEKSI(InputPengaduan.this)) {

            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                }
            });

            getJSON();
        } else {
            Toast.makeText(InputPengaduan.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

        //}else if(getStringImage(decoded) == null){
            //Toast.makeText(InputPengaduan.this, Config.ALERT_IMAGE_NOT_FOUND, Toast.LENGTH_SHORT).show();
            //Snackbar.make(v, Config.ALERT_KELUHAN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //keluhan.requestFocus();
        } else {
            @SuppressLint("StaticFieldLeak")
            class save extends AsyncTask<Void, Void, String> {
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

                    //menambah parameter yang di kirim ke web servis
                    hashMap.put(KEY_IMAGE, getStringImage(decoded));


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
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
