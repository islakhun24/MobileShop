package bowo.skripsi.mobileshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bowo.skripsi.mobileshop.adapter.API;
import bowo.skripsi.mobileshop.adapter.KategoriAdapter;
import bowo.skripsi.mobileshop.adapter.MobilAdapter;
import bowo.skripsi.mobileshop.adapter.MobilFilterAdapter;
import bowo.skripsi.mobileshop.controller.Controller;
import bowo.skripsi.mobileshop.model.KategoriModel;
import bowo.skripsi.mobileshop.model.MobilModel;

public class FilterKategoriAct extends AppCompatActivity implements MobilFilterAdapter.AdapterCallback {
    private static final String TAG = LoginAct.class.getSimpleName();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    public final static String TAG_USERNAME = "username";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String  usname;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    private ArrayList<MobilModel> mobilModelList= new ArrayList<>();
    private RecyclerView rvMobil;
    private MobilFilterAdapter mobilAdapter;
    String merk_mobil;
    EditText search;
    LinearLayout kosong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_kategori);
        merk_mobil = getIntent().getStringExtra("merk_mobil");
        rvMobil= findViewById(R.id.rvMobil);
        search =findViewById(R.id.etSearch);
        kosong = findViewById(R.id.kosong);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobilAdapter.getFilter().filter(search.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mobilAdapter = new MobilFilterAdapter(this,mobilModelList,this);
        rvMobil.setLayoutManager(new GridLayoutManager(this, 2));
        rvMobil.setItemAnimator(new DefaultItemAnimator());
        rvMobil.setAdapter(mobilAdapter);

        getMobil(merk_mobil);
    }

    private void getMobil(String merk_mobil) {
        StringRequest strReq = new StringRequest(Request.Method.GET, API.TAMPIL_MOBIL_FILTER+"?merk_mobil="+merk_mobil, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Pembelian", "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("result");
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject job = jsonArray.getJSONObject(i);
                        String id_mobil,nama_mobil, merk_mobil, deskripsi_mobil, tahun_mobil, kapasitas_mobil,harga_mobil, warna_mobil, bahan_bakar, status_mobil, gambar_mobil, stok_mobil;

                        id_mobil = job.getString("id_mobil");
                        nama_mobil = job.getString("nama_mobil");
                        merk_mobil = job.getString("merk_mobil");
                        deskripsi_mobil = job.getString("deskripsi_mobil");
                        tahun_mobil = job.getString("tahun_mobil");
                        kapasitas_mobil = job.getString("kapasitas_mobil");
                        harga_mobil = job.getString("harga_mobil");
                        warna_mobil = job.getString("warna_mobil");
                        bahan_bakar = job.getString("bahan_bakar");
                        status_mobil = job.getString("status_mobil");
                        gambar_mobil= job.getString("gambar_mobil");
                        stok_mobil = job.getString("stok_mobil");

                        MobilModel mobilModel= new MobilModel();
                        mobilModel.setId_mobil(id_mobil);
                        mobilModel.setNama_mobil(nama_mobil);
                        mobilModel.setMerk_mobil(merk_mobil);
                        mobilModel.setDeskripsi_mobil(deskripsi_mobil);
                        mobilModel.setTahun_mobil(tahun_mobil);
                        mobilModel.setKapasitas_mobil(kapasitas_mobil);
                        mobilModel.setHarga_mobil(harga_mobil);
                        mobilModel.setWarna_mobil(warna_mobil);
                        mobilModel.setBahan_bakar(bahan_bakar);
                        mobilModel.setStatus_mobil(status_mobil);
                        mobilModel.setGambar_mobil(gambar_mobil);
                        mobilModel.setStok_mobil(stok_mobil);
                        mobilModelList.add(mobilModel);

                    }
                    mobilAdapter.notifyDataSetChanged();
                    if (mobilModelList.size()<1 || mobilModelList.isEmpty()){
                        kosong.setVisibility(View.VISIBLE);
                        rvMobil.setVisibility(View.GONE);
                    }else {
                        kosong.setVisibility(View.GONE);
                        rvMobil.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Pembelian", "Login Error: " + error.getMessage());
                Toast.makeText(FilterKategoriAct.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) ;

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onItemClicked(int position) {
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        usname = sharedpreferences.getString(TAG_USERNAME, null);

        beli_mobil(position, usname);
    }

    private void beli_mobil(final int position, final String usname) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, API.TAMBAH_TRANSAKSI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


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
                params.put("id_mobil", String.valueOf(position));
                params.put("username", usname);
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

}
