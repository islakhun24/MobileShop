package bowo.skripsi.mobileshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bowo.skripsi.mobileshop.adapter.API;
import bowo.skripsi.mobileshop.adapter.MobilFilterAdapter;
import bowo.skripsi.mobileshop.controller.Controller;
import bowo.skripsi.mobileshop.model.GambarModel;
import bowo.skripsi.mobileshop.model.MobilModel;
import bowo.skripsi.mobileshop.model.TransaksiModel;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;


public class DetailMobilAct extends AppCompatActivity {
    private TextView nama,unit,tahun,kategori,harga,warna,status,kapasitas,deskripsi,bahanbakar;
    private ImageView bookmark,back;
    public String id_mobil,nama_mobil, merk_mobil, deskripsi_mobil, tahun_mobil, kapasitas_mobil,harga_mobil, warna_mobil, bahan_bakar, status_mobil, gambar_mobil, stok_mobil;
    private Button beli;
    Slider slider;
    private static final String TAG = LoginAct.class.getSimpleName();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    int messages;
    public final static String TAG_USERNAME = "username";
    List<Slide> slideList;
    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String  usname;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mobil);
        id_mobil = getIntent().getStringExtra("id_mobil");
        nama_mobil = getIntent().getStringExtra("nama_mobil");
        merk_mobil = getIntent().getStringExtra("merk_mobil");
        deskripsi_mobil = getIntent().getStringExtra("deskripsi_mobil");
        tahun_mobil = getIntent().getStringExtra("tahun_mobil");
        kapasitas_mobil = getIntent().getStringExtra("kapasitas_mobil");
        harga_mobil = getIntent().getStringExtra("harga_mobil");
        warna_mobil = getIntent().getStringExtra("warna_mobil");
        bahan_bakar = getIntent().getStringExtra("bahan_bakar");
        status_mobil = getIntent().getStringExtra("status_mobil");
        gambar_mobil = getIntent().getStringExtra("gambar_mobil");
        stok_mobil = getIntent().getStringExtra("stok_mobil");

        nama = (TextView) findViewById(R.id.nama_mobil);
        unit = (TextView) findViewById(R.id.jumlahUnit);
        tahun = (TextView) findViewById(R.id.tahun);
        kategori = (TextView) findViewById(R.id.nama_kategori);
        harga = (TextView) findViewById(R.id.harga_mobil);
        //gambar = (ImageView) findViewById(R.id.imgMobil);
        bookmark = (ImageView) findViewById(R.id.bookmark);
        back = (ImageView) findViewById(R.id.back);
        warna = (TextView) findViewById(R.id.warna);
        status = (TextView) findViewById(R.id.status);
        kapasitas = (TextView) findViewById(R.id.kapasitas);
        deskripsi = (TextView) findViewById(R.id.deskripsi);
        bahanbakar= (TextView) findViewById(R.id.bahanbakar);
        beli = (Button)findViewById(R.id.btn_beli);

        nama.setText(nama_mobil);
        unit.setText(stok_mobil+ "Unit");
        tahun.setText(tahun_mobil);
        kategori.setText(merk_mobil);
        harga.setText(harga_mobil);
      //  Picasso.with(this).load(API.GAMBAR_URL+gambar_mobil).placeholder(R.drawable.not_available).into(gambar);

        slider = findViewById(R.id.slider);

//create list of slides
         slideList= new ArrayList<>();

//handle slider click listener
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //do what you want
            }
        });

//add slides to slider

        warna.setText(warna_mobil);

        kapasitas.setText(kapasitas_mobil+" Orang");
        deskripsi.setText(deskripsi_mobil);
        status.setText(status_mobil);
        bahanbakar.setText(bahan_bakar);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        usname = sharedpreferences.getString(TAG_USERNAME, null);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             tambah_bookmark(Integer.parseInt(id_mobil),usname);
              //  Toast.makeText(DetailMobilAct.this, usname, Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                beli_mobil(Integer.parseInt(id_mobil), usname);
            }
        });
       cek_bookmark(Integer.parseInt(id_mobil),usname);
       get_gambar();
    }
    private void cek_bookmark(final int position, final String usname) {


        StringRequest strReq = new StringRequest(Request.Method.POST, API.CEK_BOOKMARK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    int status= jObj.getInt("status");
                    // Check for error node in json
                    if (success == 1) {

                       if (status==0){
                           bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                       }else {
                           bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                       }


                    } else {
                        messages = jObj.getInt(TAG_MESSAGE);
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
                        startActivity(new Intent(DetailMobilAct.this,HomeAct.class));
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

    private void tambah_bookmark(final int position, final String usname) {

        StringRequest strReq = new StringRequest(Request.Method.POST, API.TAMBAH_BOOKMARK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        cek_bookmark(position,usname);


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

    private void get_gambar() {
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        StringRequest strReq = new StringRequest(Request.Method.GET, API.TAMPIL_GAMBAR+"?id="+id_mobil, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Pembelian", "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("result");
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject job = jsonArray.getJSONObject(i);
                        String id_mobil,gambar;
                        int id_gambar;
                        id_mobil = job.getString("id_mobil");
                        gambar = API.GAMBAR_URL+job.getString("gambar");
                        id_gambar = job.getInt("id_gambar");

                        Slide mobilModel= new Slide(id_gambar,gambar,getResources().getDimensionPixelSize(R.dimen.slider_image_corner));
                        slideList.add(mobilModel);

                    }
                    slider.addSlides(slideList);
                    Log.d("Slide",slideList.get(0).getImageUrl());
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Pembelian", "Login Error: " + error.getMessage());
                Toast.makeText(DetailMobilAct.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) ;

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
