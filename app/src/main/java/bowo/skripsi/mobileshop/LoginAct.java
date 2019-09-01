package bowo.skripsi.mobileshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bowo.skripsi.mobileshop.adapter.API;
import bowo.skripsi.mobileshop.controller.Controller;

public class LoginAct extends AppCompatActivity {
    private Button login, daftar;
    private EditText usename, password;
    private ProgressDialog pDialog;
    private static final String TAG = LoginAct.class.getSimpleName();
    SharedPreferences sharedpreferences;
    Boolean session = false;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private TextView lupaPassword;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public final static String TAG_USERNAME = "username";

    String tag_json_obj = "json_obj_req";
    int success;
    ConnectivityManager conMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        login = findViewById(R.id.btnLogin);
        daftar = findViewById(R.id.btnDaftar);
        usename = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        lupaPassword = findViewById(R.id.tvLupa);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin(usename.getText().toString(),password.getText().toString());
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAct.this, DaftarAct.class));
            }
        });
        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp(v,usename.getText().toString());
            }
        });
    }
    private void checkLogin(final String user, final String pass) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(TAG_USERNAME);
                        String email = jObj.getString("email");
                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString("email",email);
                        editor.commit();

                        // Memanggil main activity
                        Intent intent = new Intent(LoginAct.this, HomeAct.class);
                        intent.putExtra(TAG_USERNAME, username);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user);
                params.put("password", pass);

                return params;
            }

        };

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void openWhatsApp(View view,String body){
        PackageManager pm=getPackageManager();
        try {

            String teks = "Saya Lupa Password dengan username *"+body+"* dan ingin menggantinya.";
            String toNumber = "628121559535";
            ;

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                    toNumber, teks))));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(LoginAct.this,"Anda Tidak Memiliki Whatsapp",Toast.LENGTH_LONG).show();

        }
    }
}
