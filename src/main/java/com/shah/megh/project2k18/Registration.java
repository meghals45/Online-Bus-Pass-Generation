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
import java.security.cert.CRLException;

import javax.xml.datatype.Duration;

public class Registration extends AppCompatActivity {

    EditText edtName, edtemail , edtphno ,edtCity , edtArea , edtExperiance ,edtpass;
    Button btnreg ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

            }

    public void init()
    {
        btnreg = findViewById(R.id.RegisterC);
        edtName = findViewById(R.id.Name);
        edtemail = findViewById(R.id.email);
        edtphno = findViewById(R.id.phno);
        edtCity = findViewById(R.id.city);
        edtArea = findViewById(R.id.area);
        edtExperiance = findViewById(R.id.experiance);
        edtpass = findViewById(R.id.pass);
    }

   public  class CReg extends AsyncTask<Void,Void,JSONObject>
   {
       JSONObject jsonObject = new JSONObject();
       JSONObject object;
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           try{
               jsonObject.put("C_name", edtName.getText().toString());
               jsonObject.put("Email", edtemail.getText().toString());
               jsonObject.put("C_ph_no", edtphno.getText().toString());
               jsonObject.put("City", edtCity.getText().toString());
               jsonObject.put("Area", edtArea.getText().toString());
               jsonObject.put("C_experiance", edtExperiance.getText().toString());
               jsonObject.put("Password", edtpass.getText().toString());


           }catch (Exception e){}
       }

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
                   Intent intent = new Intent(getApplicationContext(),ConductorHome.class);
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
       protected JSONObject doInBackground(Void... voids) {

           try {
               URL url = new URL("http://192.168.43.55:8080/Registration/reg.php");

               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setRequestProperty("Content-Type", "application/json");
               httpURLConnection.setRequestProperty("Accept", "application/json");
               httpURLConnection.setDoInput(true);
               httpURLConnection.setDoOutput(true);
               httpURLConnection.connect();

               JSONObject jsonObject = new JSONObject();


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
        CReg cr = new CReg();
        cr.execute();
    }
}
