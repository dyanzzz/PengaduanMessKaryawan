package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.DatePickerFragment;
import com.home.pengaduanmesskaryawan.config.RequestHandler;
import com.home.pengaduanmesskaryawan.config.SessionManager;
import com.home.pengaduanmesskaryawan.list.ListBlokKamar;
import com.home.pengaduanmesskaryawan.list.ListUser;

import java.util.Calendar;
import java.util.HashMap;

public class Report extends AppCompatActivity implements View.OnClickListener {

    String strKdUser, strLevel;
    private Button btnReport;
    EditText blokKamar, user, kdUser, kdKamar, from, to;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        strKdUser = intent.getStringExtra(Config.DISP_KD_USER);
        strLevel = intent.getStringExtra(Config.DISP_LEVEL_USER);

        blokKamar = findViewById(R.id.blokKamar);
        user = findViewById(R.id.user);
        kdUser = findViewById(R.id.kdUser);
        kdKamar = findViewById(R.id.kdKamar);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        btnReport = findViewById(R.id.btnReport);
        btnReport.setOnClickListener(this);

        kdUser.setVisibility(View.GONE);
        kdKamar.setVisibility(View.GONE);
        radioGroup = findViewById(R.id.radio);


    }

    public void onClick(View v) {
        if (Config.CEK_KONEKSI(Report.this)) {
            if (v == btnReport) {
                btnReport();
                //Toast.makeText(Report.this, "Ini tombol report", Toast.LENGTH_LONG).show();
            } else if (v == blokKamar) {
                Intent intent = new Intent(Report.this, ListBlokKamar.class);
                startActivityForResult(intent, Config.BLOK_KAMAR);
            } else if (v == user) {
                if (blokKamar.getText().toString().equals("")) {
                    Toast.makeText(Report.this, "* Please Input Blok Kamar", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(Report.this, "* ini tombol user", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Report.this, ListUser.class);
                    intent.putExtra(Config.DISP_BLOK_KAMAR, blokKamar.getText().toString().trim());
                    startActivityForResult(intent, Config.USER);
                }
            } else if (v == from) {
                showDatePicker("from");
            } else if (v == to) {
                showDatePicker("to");
            }
        } else {
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    private void btnReport(){

        final String strBlokKamar  = blokKamar.getText().toString().trim();
        final String strKdUser     = kdUser.getText().toString().trim();
        final String strKdKamar    = kdKamar.getText().toString().trim();
        final String strUser       = user.getText().toString().trim();
        final String strFrom       = from.getText().toString().trim();
        final String strTo         = to.getText().toString().trim();

        int selectedId              = radioGroup.getCheckedRadioButtonId();
        radioButton                 = findViewById(selectedId);

        if(strFrom.equals("")) {
            Toast.makeText(Report.this, Config.ALERT_DATE_FROM, Toast.LENGTH_SHORT).show();
            from.requestFocus();

        } else if(strTo.equals("")) {
            Toast.makeText(Report.this, Config.ALERT_DATE_TO, Toast.LENGTH_SHORT).show();
            to.requestFocus();

        } else {

            Intent intent = new Intent(getApplicationContext(), ReportResult.class);
            intent.putExtra(Config.DISP_BLOK_KAMAR, strBlokKamar);
            intent.putExtra(Config.DISP_KD_USER, strKdUser);
            intent.putExtra(Config.DISP_KD_KAMAR, strKdKamar);
            intent.putExtra(Config.DISP_NAMA, strUser);
            intent.putExtra(Config.DISP_FROM, strFrom);
            intent.putExtra(Config.DISP_TO, strTo);
            intent.putExtra(Config.DISP_STATUS_KELUHAN, radioButton.getText());
            startActivity(intent);

        }

    }

    private void showDatePicker(final String status) {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        //Toast.makeText(Report.this, status, Toast.LENGTH_SHORT).show();

        if (status.equals("from")) {
            date.setCallBack(ondateFrom);
        } else {
            date.setCallBack(ondateTo);
        }

        //date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "DatePicker");
    }

    DatePickerDialog.OnDateSetListener ondateFrom = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int bulan = month + 1;
            String day_string = String.valueOf(day);
            String month_string = String.valueOf(bulan);

            if (day < 10) {
                day_string = "0" + day;
            }
            if (bulan < 10) {
                month_string = "0" + bulan;
            }

            String stringOfDate = year + "-" + month_string + "-" + day_string;

            if (Config.CEK_KONEKSI(Report.this)) {
                from.setText(stringOfDate);
                //Toast.makeText(Report.this, status, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Report.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener ondateTo = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int bulan = month + 1;
            String day_string = String.valueOf(day);
            String month_string = String.valueOf(bulan);

            if (day < 10) {
                day_string = "0" + day;
            }
            if (bulan < 10) {
                month_string = "0" + bulan;
            }

            String stringOfDate = year + "-" + month_string + "-" + day_string;

            if (Config.CEK_KONEKSI(Report.this)) {
                to.setText(stringOfDate);
            } else {
                Toast.makeText(Report.this, Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {

            case (Config.BLOK_KAMAR) : {
                if (resultCode == Activity.RESULT_OK) {
                    String blokKamar  = data.getStringExtra(Config.DISP_BLOK_KAMAR);
                    EditText input_blokKamar    = findViewById(R.id.blokKamar);
                    EditText input_user         = findViewById(R.id.user);
                    EditText input_kdUsers      = findViewById(R.id.kdUser);
                    input_blokKamar.setText(blokKamar);
                    input_user.setText("");
                    input_kdUsers.setText("");
                }
                break;
            }
            case (Config.USER) : {
                if (resultCode == Activity.RESULT_OK) {
                    String kdUser  = data.getStringExtra(Config.DISP_KD_USER);
                    String kdKamar  = data.getStringExtra(Config.DISP_KD_KAMAR);
                    String nama  = data.getStringExtra(Config.DISP_NAMA);
                    EditText input_nama     = findViewById(R.id.user);
                    EditText input_kdUser     = findViewById(R.id.kdUser);
                    EditText input_kdKamar     = findViewById(R.id.kdKamar);
                    input_nama.setText(nama);
                    input_kdUser.setText(kdUser);
                    input_kdKamar.setText(kdKamar);
                }
                break;
            }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        //if(strLevel.equals("2")) {
        //getMenuInflater().inflate(R.menu.menu_edit, menu);
        //}
        return true;
    }
}
