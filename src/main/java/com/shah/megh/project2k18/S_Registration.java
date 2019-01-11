package com.shah.megh.project2k18;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class S_Registration extends AppCompatActivity {

    EditText edtsname , edtsaddress , edtscity , edtcno , edtcollage ,edtpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s__registration);
        init();
    }
        public void init()
        {
            edtsname = findViewById(R.id.S_Name);
            edtsaddress = findViewById(R.id.S_Address);
            edtscity = findViewById(R.id.S_City);
            edtcno = findViewById(R.id.S_CNo);
            edtcollage = findViewById(R.id.College);
            edtpass = findViewById(R.id.S_Password);
        }

        public class SReg extends AsyncTask<Void,Void,JSONObject>
        {
            JSONObject jsonObject  = new JSONObject();
            JSONObject object;

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    String msg = object.getString("res");
                    if(msg.equalsIgnoreCase("Success"))
                    {


                        String lid =  object.getString("lid");
                        String role =  object.getString("role");
                        SharedPreferences sp =  getSharedPreferences("My",MODE_PRIVATE);
                        SharedPreferences.Editor ed =  sp.edit();
                        ed.putString("lid",lid);
                        ed.putString("role",role);
                        ed.putBoolean("islogin",true);
                        ed.commit();
                        Intent intent = new Intent(getApplicationContext(),StudentHome.class);
                        startActivity(intent);
                    }

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                try {

                    jsonObject.put("S_Name", edtsname.getText().toString());
                    jsonObject.put("S_Address", edtsaddress.getText().toString());
                    jsonObject.put("S_City", edtscity.getText().toString());
                    jsonObject.put("S_CNo", edtcno.getText().toString());
                    jsonObject.put("College", edtcollage.getText().toString());
                    jsonObject.put("S_Password", edtpass.getText().toString());

                }
            catch (Exception Ex){
                    Ex.printStackTrace();
            }}

            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://192.168.43.55:8080/Registration/sreg.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                //    JSONObject jsonObject = new JSONObject();

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
                         object = new JSONObject(stringBuilder.toString());

                    }
                } catch (Exception e) {

                }
                return object;
            }
            }

        public void api(View view) {
            SReg sr = new SReg();
            sr.execute();

        }
}
