package bowo.skripsi.mobileshop.fragmen;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Map;

import bowo.skripsi.mobileshop.LoginAct;
import bowo.skripsi.mobileshop.R;
import bowo.skripsi.mobileshop.adapter.API;
import bowo.skripsi.mobileshop.adapter.KategoriAdapter;
import bowo.skripsi.mobileshop.adapter.MobilAdapter;
import bowo.skripsi.mobileshop.controller.Controller;
import bowo.skripsi.mobileshop.model.KategoriModel;
import bowo.skripsi.mobileshop.model.MobilModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment implements MobilAdapter.AdapterCallback {
    private ArrayList<KategoriModel> kategoriModelList= new ArrayList<>();
    private ArrayList<MobilModel> mobilModelList= new ArrayList<>();
    private RecyclerView rvKategori,rvMobil;
    private KategoriAdapter kategoriAdapter;
    private MobilAdapter mobilAdapter;
    private static final String TAG = LoginAct.class.getSimpleName();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    public final static String TAG_USERNAME = "username";
    private EditText search;
    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String  usname;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    LinearLayout kosong;
    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rvKategori= v.findViewById(R.id.rvKategori);
        rvMobil= v.findViewById(R.id.rvMobil);
        kosong = v.findViewById(R.id.kosong);
        kategoriAdapter = new KategoriAdapter(getActivity(),kategoriModelList);
        mobilAdapter = new MobilAdapter(getActivity(),mobilModelList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        rvKategori.setLayoutManager(mLayoutManager);
        rvMobil.setLayoutManager(mLayoutManager1);
        rvKategori.setItemAnimator(new DefaultItemAnimator());
        rvMobil.setItemAnimator(new DefaultItemAnimator());
        rvKategori.setAdapter(kategoriAdapter);
        rvMobil.setAdapter(mobilAdapter);
        search = v.findViewById(R.id.etSearch);
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
        getKategori();
        getMobil();
        return v;
    }

    private void getMobil() {
        StringRequest strReq = new StringRequest(Request.Method.GET, API.TAMPIL_MOBIL, new Response.Listener<String>() {

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
                    kategoriAdapter.notifyDataSetChanged();
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
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) ;

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void getKategori() {
        StringRequest strReq = new StringRequest(Request.Method.GET, API.TAMPIL_KATEGORI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Pembelian", "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("result");
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject job = jsonArray.getJSONObject(i);
                        String id,nama;
                        id = job.getString("id");
                        nama = job.getString("nama");

                        KategoriModel kategoriModel= new KategoriModel();
                        kategoriModel.setId(id);
                        kategoriModel.setNama(nama);
                        kategoriModelList.add(kategoriModel);

                    }
                    kategoriAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Pembelian", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) ;

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onItemClicked(int position) {
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        usname = sharedpreferences.getString(TAG_USERNAME, null);

        beli_mobil(position, usname);
    }

    private void beli_mobil(final int position, final String usname) {
        pDialog = new ProgressDialog(getContext());
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

                        Toast.makeText(getContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(getContext(),
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
                Toast.makeText(getContext(),
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
