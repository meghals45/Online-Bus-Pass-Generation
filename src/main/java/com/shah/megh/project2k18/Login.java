package com.shah.megh.project2k18;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.*;

public class Login extends AppCompatActivity {

    Button button, buttonsuc;
    LinearLayout llm, mysuc, overbox;
    ImageView imageView, suciv;
    Animation fromsmall, fromnothing, forsuc, togo;
    EditText edtusrnm, edtpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        button = findViewById(R.id.button);
        buttonsuc = findViewById(R.id.buttonsuc);

        llm = findViewById(R.id.llm);
        mysuc = findViewById(R.id.mysuc);
        overbox = findViewById(R.id.overbox);

        imageView = findViewById(R.id.imageView);
        suciv = findViewById(R.id.suciv);

        fromsmall = AnimationUtils.loadAnimation(this, R.anim.fromsmall);
        fromnothing = AnimationUtils.loadAnimation(this, R.anim.fromnothing);
        forsuc = AnimationUtils.loadAnimation(this, R.anim.forsuc);
        togo = AnimationUtils.loadAnimation(this, R.anim.togo);

        mysuc.setAlpha(0);
        overbox.setAlpha(0);
        suciv.setVisibility(GONE);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoGin loGin = new LoGin();
                loGin.execute();
              //  api();
//                suciv.setVisibility(VISIBLE);
//                suciv.startAnimation(forsuc);
//
//                overbox.setAlpha(1);
//                overbox.startAnimation(fromnothing);
//
//                mysuc.setAlpha(1);
//                mysuc.startAnimation(fromsmall);
            }
        });

        buttonsuc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                overbox.startAnimation(togo);
                mysuc.startAnimation(togo);
                suciv.startAnimation(togo);
                suciv.setVisibility(View.GONE);

                ViewCompat.animate(overbox).setStartDelay(1000).alpha(0).start();
                ViewCompat.animate(mysuc).setStartDelay(1000).alpha(0).start();
                //   ViewCompat.animate(suciv).setStartDelay(1000).alpha(0).start();
            }
        });


    }

    public void init() {
        edtusrnm = findViewById(R.id.userName);
        edtpass = findViewById(R.id.Password);
    }

    public class LoGin extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jsonObject = new JSONObject();
        JSONObject object;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                jsonObject.put("L_name", edtusrnm.getText().toString());
                jsonObject.put("L_password", edtpass.getText().toString());
                // jsonObject.put("S_City", edtscity.getText().toString());
            } catch (Exception e) {
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                String msg = object.getString("res");
                if (msg.equalsIgnoreCase("Success")) {
                    String lid = object.getString("lid");
                    String role = object.getString("role");
                    SharedPreferences sp = getSharedPreferences("My", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("lid", lid);
                    ed.putString("role", role);
                    ed.putBoolean("islogin", true);
                    ed.commit();
                    if (role.equalsIgnoreCase("student")) {

                        Intent intent = new Intent(getApplicationContext(), StudentHome.class);
                        startActivity(intent);
                    } else if (role.equalsIgnoreCase("Conductor")) {
                        Intent intent = new Intent(getApplicationContext(), ConductorHome.class);
                        startActivity(intent);
                    } else if (role.equalsIgnoreCase("passenger")) {
                        Intent intent = new Intent(getApplicationContext(), PassengerHome.class);
                        startActivity(intent);
                    }

                }

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected JSONObject doInBackground(Void... voids) {


            try {
                URL url = new URL("http://192.168.43.55:8080/Registration/LOGIN.php");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                outputStream.write(jsonObject.toString().getBytes());

                int code = httpURLConnection.getResponseCode();
                if (code == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String s;

                    while ((s = bufferedReader.readLine()) != null) {
                        stringBuilder.append(s);

                    }
                    object= new JSONObject(stringBuilder.toString());


//                    //  S_Registration sr = new S_Registration();
//                    if (msg.equalsIgnoreCase("Success")) {
//
//                        String lid = object.getString("lid");
//                        String role = object.getString("role");
//
//                        SharedPreferences sp = getSharedPreferences("My", MODE_PRIVATE);
//                        SharedPreferences.Editor ed = sp.edit();
//                        ed.putString("lid", lid);
//                        ed.putString("role", role);
//                        ed.putBoolean("islogin", true);
//                        ed.commit();
//                                      }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return object;
        }


        public void api(View view) {
            LoGin lg = new LoGin();
            lg.execute();

        }
    }
}
