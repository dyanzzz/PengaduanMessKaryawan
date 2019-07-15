package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.adapter.TaskAdapterPengaduan;
import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.RecyclerTouchListener;
import com.home.pengaduanmesskaryawan.config.RequestHandler;
import com.home.pengaduanmesskaryawan.model.ModelTaskPengaduan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayPengaduan extends AppCompatActivity {

    String strKdKamar, strKdUser, strKdTombol, strLevelUser;
    private List<ModelTaskPengaduan> modalTaskList = new ArrayList<>();
    private TaskAdapterPengaduan mAdapter;
    private String JSON_STRING;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pengaduan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        strKdUser = intent.getStringExtra(Config.DISP_KD_USER);
        strKdKamar = intent.getStringExtra(Config.DISP_KD_KAMAR);
        strKdTombol = intent.getStringExtra(Config.DISP_KD_TOMBOL);
        strLevelUser = intent.getStringExtra(Config.DISP_LEVEL_USER);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new TaskAdapterPengaduan(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DisplayPengaduan.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(DisplayPengaduan.this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (Config.CEK_KONEKSI(DisplayPengaduan.this)) {
                    ModelTaskPengaduan modalTask = modalTaskList.get(position);
                    if ((strLevelUser.equals("2") && modalTask.getStatusKeluhan().equals("Menunggu")) || (strLevelUser.equals("1") && !modalTask.getStatusKeluhan().equals("Selesai"))) {
                        if (!modalTask.getKdUser().equals("0")) {
                            final String kdUser = modalTask.getKdUser();
                            final String kdKamar = modalTask.getKdKamar();
                            final String noKamar = modalTask.getNoKamar();
                            final String blokKamar = modalTask.getBlokKamar();
                            final String keluhan = modalTask.getKeluhan();
                            final String kdKeluhan = modalTask.getKdKeluhan();
                            final String nama = modalTask.getNama();

                            if( strLevelUser.equals("1")) {
                                if(modalTask.getStatusKeluhan().equals("Menunggu")) {

                                    final CharSequence[] dialogitem = {"- Proses Keluhan"};
                                    dialog = new AlertDialog.Builder(DisplayPengaduan.this);
                                    dialog.setCancelable(true);
                                    //dialog.setTitle("Keluhan");
                                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            switch (which) {
                                                case 0:
                                                    if (Config.CEK_KONEKSI(DisplayPengaduan.this)) {
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayPengaduan.this);
                                                        alertDialogBuilder.setMessage("Proses keluhan?");
                                                        alertDialogBuilder.setCancelable(false);
                                                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                //Back to Login.class
                                                                btnProsesKeluhan(kdKeluhan, "Proses");
                                                                //Toast.makeText(DisplayPengaduan.this, "proses keluhan", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                            }
                                                        });

                                                        //Showing the alert dialog
                                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                                        alertDialog.show();

                                                    } else {
                                                        //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                        Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_NO_CONN, Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;

                                            }
                                        }
                                    }).show();
                                } else {
                                    final CharSequence[] dialogitem = {"- Keluhan Selesai"};
                                    dialog = new AlertDialog.Builder(DisplayPengaduan.this);
                                    dialog.setCancelable(true);
                                    //dialog.setTitle("Keluhan");
                                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            switch (which) {

                                                case 0:
                                                    if (Config.CEK_KONEKSI(DisplayPengaduan.this)) {
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayPengaduan.this);
                                                        alertDialogBuilder.setMessage("Keluhan selesai?");
                                                        alertDialogBuilder.setCancelable(false);
                                                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                //Back to Login.class
                                                                btnProsesKeluhan(kdKeluhan, "Selesai");
                                                                //Toast.makeText(DisplayPengaduan.this, "keluhan selesai", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                            }
                                                        });

                                                        //Showing the alert dialog
                                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                                        alertDialog.show();

                                                    } else {
                                                        //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                        Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_NO_CONN, Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;

                                            }
                                        }
                                    }).show();
                                }

                            }else{

                                final CharSequence[] dialogitem = {"- Delete Keluhan"};
                                dialog = new AlertDialog.Builder(DisplayPengaduan.this);
                                dialog.setCancelable(true);
                                //dialog.setTitle("Keluhan");
                                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        switch (which) {
                                            case 0:
                                                if (Config.CEK_KONEKSI(DisplayPengaduan.this)) {
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayPengaduan.this);
                                                    alertDialogBuilder.setMessage("Are you sure to delete?");
                                                    alertDialogBuilder.setCancelable(false);
                                                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                            //Back to Login.class
                                                            btnDelete(kdKeluhan);
                                                            //Toast.makeText(DisplayPengaduan.this, blokKamar, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                        }
                                                    });

                                                    //Showing the alert dialog
                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();

                                                } else {
                                                    //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                    Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_NO_CONN, Toast.LENGTH_SHORT).show();
                                                }
                                                break;

                                        }
                                    }
                                }).show();
                            }
                        } else {
                            Toast.makeText(DisplayPengaduan.this, modalTask.getNama(), Toast.LENGTH_SHORT).show();
                            //Snackbar.make(view, modalTask.getBlokKamar(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                    }
                } else {
                    Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
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
                if (Config.CEK_KONEKSI(DisplayPengaduan.this)) {
                    modalTaskList.clear();
                    getJSON();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    modalTaskList.clear();
                    Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (Config.CEK_KONEKSI(DisplayPengaduan.this)) {
            getJSON();
        } else {
            //onCreateDialog(tampil_error);
            //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
            Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void btnProsesKeluhan(final String kdKeluhan, final String statusKeluhan) {
        @SuppressLint("StaticFieldLeak")
        class getJSONDel extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DisplayPengaduan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DisplayPengaduan.this, s, Toast.LENGTH_LONG).show();

                modalTaskList.clear();
                //String stringOfDate = "";
                getJSON();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from form input
                params.put(Config.DISP_KD_KELUHAN, kdKeluhan);
                params.put(Config.DISP_KD_TOMBOL, statusKeluhan);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ACTION_KELUHAN, params);
                return res;
            }
        }

        getJSONDel aw = new getJSONDel();
        aw.execute();
    }

    private void btnDelete(final String kdKeluhan) {
        @SuppressLint("StaticFieldLeak")
        class getJSONDel extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DisplayPengaduan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DisplayPengaduan.this, s, Toast.LENGTH_LONG).show();

                modalTaskList.clear();
                //String stringOfDate = "";
                getJSON();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from form input
                params.put(Config.DISP_KD_KELUHAN, kdKeluhan);
                params.put(Config.DISP_KD_TOMBOL, "delete");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ACTION_KELUHAN, params);
                return res;
            }
        }

        getJSONDel aw = new getJSONDel();
        aw.execute();
    }

    private void getJSON() {
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DisplayPengaduan.this, "", Config.ALERT_PLEASE_WAIT, false, false);
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
                params.put(Config.DISP_KD_TOMBOL, strKdTombol);
                params.put(Config.DISP_LEVEL_USER, strLevelUser);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_KELUHAN, params);
                return res;
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

                ModelTaskPengaduan modalTask = new ModelTaskPengaduan(nomor, kdUser, kdKamar, kdKeluhan, blokKamar, noKamar, nama, keluhan, tanggalKeluhan, statusKeluhan);
                modalTaskList.add(modalTask);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(DisplayPengaduan.this, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Toast.LENGTH_SHORT).show();
            //Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        mAdapter.notifyDataSetChanged();

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
