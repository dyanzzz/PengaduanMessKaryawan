package com.home.pengaduanmesskaryawan;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView textView, versiApp;
    SessionManager session;
    String fullname, usercode, access_level;
    NavigationView navigation_view;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_icon);
        toolbar.setLogo(R.mipmap.pengaduan_mess_karyawan);
        toolbar.setLogoDescription(R.string.app_name);

        session = new SessionManager(getApplicationContext());
        String statusLogin = String.valueOf(session.isLoggedIn());
        if(statusLogin.equals("false")){
            onBackPressed();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        fullname        = user.get(SessionManager.KEY_NAMAUSER);
        usercode        = user.get(SessionManager.KEY_USERNAME);
        access_level    = user.get(SessionManager.KEY_LEVEL);

        LinearLayout input_kamar        = findViewById(R.id.layout_input_kamar);
        LinearLayout display_kamar      = findViewById(R.id.layout_display_kamar);
        LinearLayout input_pengaduan    = findViewById(R.id.layout_input_pengaduan);
        LinearLayout display_pengaduan  = findViewById(R.id.layout_display_pengaduan);

        if(access_level.equals("1")){
            input_pengaduan.setVisibility(View.GONE);
        }
        if(access_level.equals("2")){
            input_kamar.setVisibility(View.GONE);
            display_kamar.setVisibility(View.GONE);
        }

        //Initializing textview
        textView    = findViewById(R.id.welcome);
        versiApp    = findViewById(R.id.versiApp);

        //Showing the current logged to textview
        textView.setText("Welcome " + fullname);
        versiApp.setText("Version " + Config.VALUE_VERSI);

        drawerLayout    = findViewById(R.id.drawer);
        navigation_view = findViewById(R.id.navigation_view);

        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, navigation_view, false);
        navigation_view.addHeaderView(headerView);
        TextView name   = headerView.findViewById(R.id.name);
        name.setText(fullname);

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    /*
                    case R.id.menuInbox:
                        if(Config.CEK_KONEKSI(MainActivity.this)) {
                            Intent account = new Intent(MainActivity.this, Inbox.class);
                            startActivity(account);
                        }else{
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;
                    */

                    case R.id.menuProfil:
                        if(Config.CEK_KONEKSI(MainActivity.this)) {
                            Intent intent = new Intent(MainActivity.this, Profil.class);
                            intent.putExtra(Config.DISP_KD_USER, usercode);
                            intent.putExtra(Config.DISP_KD_KAMAR, "0");
                            intent.putExtra(Config.DISP_LEVEL_USER, access_level);
                            startActivity(intent);
                        }else{
                            showDialog(Config.TAMPIL_ERROR);
                        }
                        return true;

                    case R.id.menuLogout:
                        logout();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),Config.ALERT_TITLE_CONN_ERROR,Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });
    }

    //Get ID tombol & Event Tombol
    public void layout_btn_input_kamar(View view) {
        if(Config.CEK_KONEKSI(MainActivity.this)) {
            Intent intent = new Intent(MainActivity.this, InputKamar.class);
            intent.putExtra(Config.DISP_KD_USER, usercode);
            intent.putExtra(Config.DISP_KD_KAMAR, "0");
            startActivity(intent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void layout_btn_display_kamar(View view) {
        if(Config.CEK_KONEKSI(MainActivity.this)) {
            Intent intent = new Intent(getApplicationContext(), DisplayKamar.class);
            startActivity(intent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void layout_btn_input_pengaduan(View view) {
        if(Config.CEK_KONEKSI(MainActivity.this)) {
            Intent intent = new Intent(getApplicationContext(), InputPengaduan.class);
            intent.putExtra(Config.DISP_KD_USER, usercode);
            intent.putExtra(Config.DISP_KD_KAMAR, "0");
            intent.putExtra(Config.DISP_KD_TOMBOL, "detail");
            startActivity(intent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    public void layout_btn_display_pengaduan(View view) {
        if(Config.CEK_KONEKSI(MainActivity.this)) {
            Intent intent = new Intent(getApplicationContext(), DisplayPengaduan.class);
            intent.putExtra(Config.DISP_KD_USER, usercode);
            intent.putExtra(Config.DISP_KD_KAMAR, "0");
            intent.putExtra(Config.DISP_KD_TOMBOL, "all");
            intent.putExtra(Config.DISP_LEVEL_USER, access_level);
            startActivity(intent);
        }else{
            showDialog(Config.TAMPIL_ERROR);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case Config.TAMPIL_ERROR:
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
            default:
                break;
        }
        return null;
    }


    //Logout function
    private void logout(){

        //Creating an alert dialog to confirm logout
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Are you sure want to Logout?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Back to Login.class
                onBackPressed();
            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Welcome "+fullname, Toast.LENGTH_SHORT).show();
            }
        });

        //Showing the alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.list_toolbar) {
            drawerLayout.openDrawer(Gravity.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
