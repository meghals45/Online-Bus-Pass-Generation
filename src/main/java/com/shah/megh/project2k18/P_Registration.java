package com.shah.megh.project2k18;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class P_Registration extends AppCompatActivity {

    EditText edtpname ,edtpemail , edtpphno ,edtdob , edtpass ;
    Button btnpreg ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p__registration);
        init();
        btnpreg = findViewById(R.id.Registerp);
    }

    public void init()
    {
        edtpname = findViewById(R.id.P_name);
        edtpemail = findViewById(R.id.P_email);
        edtpphno = findViewById(R.id.P_ph_no);
        edtdob = findViewById(R.id.P_DOB);
        edtpass = findViewById(R.id.pass);
    }
    public  class PReg extends AsyncTask<Void,Void,JSONObject>
    {
        JSONObject jsonObject = new JSONObject();
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
                    Intent intent = new Intent(getApplicationContext(),PassengerHome.class);
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
                jsonObject.put("P_name", edtpname.getText().toString());
                jsonObject.put("P_email", edtpemail.getText().toString());
                jsonObject.put("P_ph_no", edtpphno.getText().toString());
                jsonObject.put("P_DOB", edtdob.getText().toString());
                jsonObject.put("P_Password",edtpass.getText().toString());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
                    }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.43.55:8080/Registration/Preg.php");

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
                     object = new JSONObject(stringBuilder.toString());
                }
            } catch (Exception e) {
            }
            return object;
        }
    }
    public void api(View view) {
      PReg pr = new PReg();
        pr.execute();
    }
}