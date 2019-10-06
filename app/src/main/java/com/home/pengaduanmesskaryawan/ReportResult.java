package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.home.pengaduanmesskaryawan.adapter.TaskAdapterPengaduan;
import com.home.pengaduanmesskaryawan.adapter.TaskAdapterReportResult;
import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.RecyclerTouchListener;
import com.home.pengaduanmesskaryawan.config.RequestHandler;
import com.home.pengaduanmesskaryawan.model.ModelTaskPengaduan;
import com.home.pengaduanmesskaryawan.model.ModelTaskReportResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportResult extends AppCompatActivity {

    String strKdKamar, strKdUser, strNama, strBlokKamar, strFrom, strTo, strStatusKeluhan;
    private List<ModelTaskReportResult> modalTaskList = new ArrayList<>();
    private TaskAdapterReportResult mAdapter;
    private String JSON_STRING;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AlertDialog.Builder dialog;
    public String searchPublic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_result);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        strKdUser = intent.getStringExtra(Config.DISP_KD_USER);
        strNama = intent.getStringExtra(Config.DISP_NAMA);
        strBlokKamar = intent.getStringExtra(Config.DISP_BLOK_KAMAR);
        strFrom = intent.getStringExtra(Config.DISP_FROM);
        strTo = intent.getStringExtra(Config.DISP_TO);
        strStatusKeluhan = intent.getStringExtra(Config.DISP_STATUS_KELUHAN);
        strKdKamar = intent.getStringExtra(Config.DISP_KD_KAMAR);

        //Toast.makeText(ReportResult.this, strStatusKeluhan+"-"+strBlokKamar+"-"+strKdUser+"-"+strKdKamar+"-"+strNama+"-"+strFrom+"-"+strTo, Toast.LENGTH_LONG).show();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new TaskAdapterReportResult(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportResult.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.destroyDrawingCache();
        imagePopup.clearFocus();
        imagePopup.setBackgroundColor(Color.WHITE);
//        imagePopup.setWindowHeight(800);
//        imagePopup.setWindowWidth(800);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ReportResult.this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (Config.CEK_KONEKSI(ReportResult.this)) {
                    ModelTaskReportResult modalTask = modalTaskList.get(position);

                    final String kdUser = modalTask.getKdUser();
                    final String kdKamar = modalTask.getKdKamar();
                    final String noKamar = modalTask.getNoKamar();
                    final String blokKamar = modalTask.getBlokKamar();
                    final String keluhan = modalTask.getKeluhan();
                    final String kdKeluhan = modalTask.getKdKeluhan();
                    final String nama = modalTask.getNama();
                    final String image = modalTask.getImage();

                    final CharSequence[] dialogitem = {"- View Image Complain"};
                    dialog = new AlertDialog.Builder(ReportResult.this);
                    dialog.setCancelable(true);
                    //dialog.setTitle("Keluhan");
                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            imagePopup.initiatePopupWithPicasso(image);
                            imagePopup.viewPopup();
                        }
                    }).show();


                } else {
                    Toast.makeText(ReportResult.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //untuk refresh
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Config.CEK_KONEKSI(ReportResult.this)) {
                    modalTaskList.clear();
                    getJSON();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    modalTaskList.clear();
                    Toast.makeText(ReportResult.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (Config.CEK_KONEKSI(ReportResult.this)) {

            getJSON();
        } else {
            //onCreateDialog(tampil_error);
            //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
            Toast.makeText(ReportResult.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void getJSON() {
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ReportResult.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(DaftarMasjid.this, s, Toast.LENGTH_SHORT).show();
                showData();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_USER, strKdUser);
                params.put(Config.DISP_KD_KAMAR, strKdKamar);
                params.put(Config.DISP_BLOK_KAMAR, strBlokKamar);
                params.put(Config.DISP_FROM, strFrom);
                params.put(Config.DISP_TO, strTo);
                params.put(Config.DISP_STATUS_KELUHAN, strStatusKeluhan);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_REPORT_RESULT, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showData() {
        JSONObject jsonObject = null;
        //ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String nomor = jo.getString(Config.DISP_NOMOR);
                String kdUser = jo.getString(Config.DISP_KD_USER);
                String username = jo.getString(Config.DISP_USERNAME);
                String blokKamar = jo.getString(Config.DISP_BLOK_KAMAR);
                String noKamar = jo.getString(Config.DISP_NO_KAMAR);
                String nama = jo.getString(Config.DISP_NAMA);
                String kdKamar = jo.getString(Config.DISP_KD_KAMAR);
                String kdKeluhan = jo.getString(Config.DISP_KD_KELUHAN);
                String keluhan = jo.getString(Config.DISP_KELUHAN);
                String tanggalKeluhan = jo.getString(Config.DISP_TANGGAL_KELUHAN);
                String statusKeluhan = jo.getString(Config.DISP_STATUS_KELUHAN);
                String image = jo.getString(Config.DISP_IMAGE);

                ModelTaskReportResult modalTask = new ModelTaskReportResult(nomor, kdUser, kdKamar, kdKeluhan, blokKamar, noKamar, nama, keluhan, tanggalKeluhan, statusKeluhan, image);
                modalTaskList.add(modalTask);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ReportResult.this, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Toast.LENGTH_SHORT).show();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        mAdapter.notifyDataSetChanged();

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
