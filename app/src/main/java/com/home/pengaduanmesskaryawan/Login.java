package com.home.pengaduanmesskaryawan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.home.pengaduanmesskaryawan.config.Config;
import com.home.pengaduanmesskaryawan.config.JSONParser;
import com.home.pengaduanmesskaryawan.config.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    SessionManager session;
    Button login;
    EditText username, password;
    String success, message, linkUpdate, user, pass, versi;
    Switch showHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        session = new SessionManager(Login.this);
        String statusLogin = String.valueOf(session.isLoggedIn());
        username        = findViewById(R.id.etUsername);
        password        = findViewById(R.id.etPassword);
        login           = findViewById(R.id.login);

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.CEK_KONEKSI(Login.this)) {
                    user        = username.getText().toString().trim();
                    pass        = password.getText().toString().trim();
                    versi       = Config.VALUE_VERSI;

                    if (user.length() > 0 && pass.length() > 0) {
                        new PostAsync().execute(user, pass, versi);
                    } else if (user.length() == 0) {
                        Toast.makeText(getApplicationContext(), Config.ALERT_USERNAME, Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getApplicationContext(), Config.ALERT_PASSWORD, Toast.LENGTH_LONG).show();
                        //password.setError(Config.ALERT_PASSWORD);
                        //Snackbar.make(v, Config.ALERT_PASSWORD, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Toast.makeText(getApplicationContext(), Config.ALERT_PASSWORD, Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Snackbar.make(v, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Toast.makeText(getApplicationContext(), Config.ALERT_MESSAGE_NO_CONN, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //method POST
    @SuppressLint("StaticFieldLeak")
    private class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("");
            pDialog.setMessage(Config.ALERT_PLEASE_WAIT);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USERNAME, args[0]);
                params.put(Config.KEY_PASSWORD, args[1]);
                params.put(Config.KEY_VERSI, args[2]);

                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(Config.LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            success = "0";
            message = "";
            //linkUpdate = "";

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (json != null) {
                try {
                    success = json.getString("success");
                    message = json.getString("message");
                    //linkUpdate = json.getString("linkUpdate");
                    JSONArray hasil = json.getJSONArray("login");
                    if (success.equals("1")){
                        //Toast.makeText(getActivity(), json.toString(), Toast.LENGTH_LONG).show();

                        for (int i = 0; i < hasil.length(); i++){
                            JSONObject c = hasil.getJSONObject(i);
                            String user_name    = c.getString("username").trim();
                            String nama         = c.getString("nama").trim();
                            String level        = c.getString("level").trim();
                            session.createLoginSession(user_name, nama, level);
                            Log.e("ok", " ambil data");
                        }

                        //} else if(success.equals("3")){
                        //Update Apps
                        //Log.e("error", "tidak bisa ambil data 1");
                        //buttonUpdate.setVisibility(View.VISIBLE);
                    } else {
                        //untuk display json from server
                        //Toast.makeText(Login.this, json.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error", "tidak bisa ambil data 1");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success.equals("1")) {
                Log.d("Success!", message);
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                //finish();
            }else{
                Log.d("Failure", message);
                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

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
