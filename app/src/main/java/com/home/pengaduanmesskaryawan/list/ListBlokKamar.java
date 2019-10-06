package com.home.pengaduanmesskaryawan.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.home.pengaduanmesskaryawan.R;
import com.home.pengaduanmesskaryawan.adapter.TaskAdapterBlokKamar;
import com.home.pengaduanmesskaryawan.config.AppController;
import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.model.ModelTaskBlokKamar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBlokKamar extends AppCompatActivity
        implements ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    String fullname, usercode, nip, branch_code, dept_code, company_code, access_level, salesman_code,
            supervisor_code, dealer_code;

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ModelTaskBlokKamar> modelList = new ArrayList<>();
    private static final String TAG = ListBlokKamar.class.getSimpleName();

    Handler handler;
    Runnable runnable;
    int no;
    TaskAdapterBlokKamar adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_blok_kamar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView = findViewById(R.id.list_blok_kamar);

        modelList.clear();
        listView.setOnItemClickListener(this);

        adapter = new TaskAdapterBlokKamar(ListBlokKamar.this, modelList);
        listView.setAdapter(adapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(ListBlokKamar.this)) {
                    //mSwipeRefreshLayout.setRefreshing(true);
                    modelList.clear();
                    adapter.notifyDataSetChanged();
                    getJSON();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showDialog(Config.TAMPIL_ERROR);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(ListBlokKamar.this)) {
            modelList.clear();
            adapter.notifyDataSetChanged();
            getJSON();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showDialog(Config.TAMPIL_ERROR);
        }
    }


    private void getJSON() {
        //mSwipeRefreshLayout.setRefreshing(true);

        final ProgressDialog loading = ProgressDialog.show(ListBlokKamar.this, "", Config.ALERT_PLEASE_WAIT, false, false);
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_BLOK_KAMAR,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);

                        if (response.length() > 0) {
                            // Parsing json
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    //JSONObject obj = response.getJSONObject(i);
                                    ModelTaskBlokKamar ambil = new ModelTaskBlokKamar();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    ambil.setNomor(obj.getString(Config.DISP_NOMOR));
                                    ambil.setBlokKamar(obj.getString(Config.DISP_BLOK_KAMAR));

                                    // adding news to news array
                                    modelList.add(ambil);

                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();

                        }
                        mSwipeRefreshLayout.setRefreshing(false);

                        loading.dismiss();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListBlokKamar.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put(Config.KEY_SEARCH, search);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Config.CEK_KONEKSI(ListBlokKamar.this)) {
            String blokKamar = modelList.get(position).getBlokKamar();
            if (!blokKamar.equals("0")) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Config.DISP_BLOK_KAMAR, blokKamar);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }



    //dialog disconect
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Config.TAMPIL_ERROR) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
            errorDialog.setTitle(Config.ALERT_TITLE_CONN_ERROR);
            errorDialog.setMessage(Config.ALERT_MESSAGE_CONN_ERROR);
            errorDialog.setNeutralButton(Config.ALERT_OK_BUTTON,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );
            return errorDialog.create();
        }
        return null;
    }

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.toolbar_select_all:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Config.DISP_BLOK_KAMAR, "");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_all, menu);

        // return true so that the menu pop up is opened
        return true;
    }




}
