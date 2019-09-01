package bowo.skripsi.mobileshop.fragmen;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import bowo.skripsi.mobileshop.R;
import bowo.skripsi.mobileshop.adapter.API;
import bowo.skripsi.mobileshop.adapter.BookmarkAdapter;
import bowo.skripsi.mobileshop.controller.Controller;
import bowo.skripsi.mobileshop.model.BookmarkModel;
import bowo.skripsi.mobileshop.model.TransaksiModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFrag extends Fragment {
    private ArrayList<BookmarkModel> bookmarkModels= new ArrayList<>();

    private RecyclerView rvMobil;
    private BookmarkAdapter bookmarkAdapter;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    public final static String TAG_USERNAME = "username";
    private LinearLayout kosong;
    String tag_json_obj = "json_obj_req";



    SharedPreferences sharedpreferences;
    Boolean session = false;
    String  usname;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    public BookmarkFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bookmark, container, false);
        rvMobil= v.findViewById(R.id.rvMobil);

        bookmarkAdapter = new BookmarkAdapter(getActivity(),bookmarkModels);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);

        rvMobil.setLayoutManager(mLayoutManager1);
        rvMobil.setItemAnimator(new DefaultItemAnimator());
        rvMobil.setAdapter(bookmarkAdapter);
        kosong = v.findViewById(R.id.kosong);
        getMobil();
        return v;
    }
    private void getMobil() {
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        usname = sharedpreferences.getString(TAG_USERNAME, null);
        StringRequest strReq = new StringRequest(Request.Method.GET, API.TAMPIL_BOOKMARK+"?username="+usname, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Pembelian", "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("result");
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject job = jsonArray.getJSONObject(i);
                        String id_mobil,nama_mobil, merk_mobil, deskripsi_mobil, tahun_mobil, kapasitas_mobil,harga_mobil, warna_mobil, bahan_bakar, status_mobil, gambar_mobil, stok_mobil;
                        int stat;
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
                        stat = job.getInt("status");
                        BookmarkModel mobilModel= new BookmarkModel();
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
                        mobilModel.setStatus(stat);
                        bookmarkModels.add(mobilModel);

                    }
                    bookmarkAdapter.notifyDataSetChanged();
                    if (bookmarkModels.size()<1 || bookmarkModels.isEmpty()){
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
}
