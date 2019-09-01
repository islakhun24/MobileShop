package bowo.skripsi.mobileshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import bowo.skripsi.mobileshop.DetailMobilAct;
import bowo.skripsi.mobileshop.R;
import bowo.skripsi.mobileshop.model.BookmarkModel;
import bowo.skripsi.mobileshop.model.TransaksiModel;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    Context context;
    private ArrayList<BookmarkModel> item;

    public BookmarkAdapter(Context context, ArrayList<BookmarkModel> item) {

        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false); //memanggil layout list recyclerview
        ViewHolder processHolder = new ViewHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final BookmarkModel data = item.get(position);
        holder.nama.setText(data.getNama_mobil());
        holder.kategori.setText(data.getMerk_mobil()+" - "+data.getTahun_mobil());
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        final int harga_mobil = Integer.parseInt(data.getHarga_mobil());

        holder.harga.setText(formatRupiah.format((double)harga_mobil));
        Picasso.with(context).load(API.GAMBAR_URL+data.getGambar_mobil()).placeholder(R.drawable.not_available).into(holder.gambar);
        //menampilkan data
        holder.konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailMobilAct.class);
                i.putExtra("stok_mobil",data.getStok_mobil());
                i.putExtra("id_mobil",data.getId_mobil());
                i.putExtra("nama_mobil",data.getNama_mobil());
                i.putExtra("merk_mobil",data.getMerk_mobil());
                i.putExtra("deskripsi_mobil",data.getDeskripsi_mobil());
                i.putExtra("tahun_mobil",data.getTahun_mobil());
                i.putExtra("kapasitas_mobil",data.getKapasitas_mobil());
                i.putExtra("harga_mobil",formatRupiah.format((double)harga_mobil));
                i.putExtra("warna_mobil",data.getWarna_mobil());
                i.putExtra("bahan_bakar",data.getBahan_bakar());
                i.putExtra("status_mobil",data.getStatus_mobil());
                i.putExtra("gambar_mobil",data.getGambar_mobil());
                i.putExtra("status",data.getStatus());
                context.startActivities(new Intent[]{i});
            }
        });



    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,kategori,harga;
        RelativeLayout konfirmasi;
        ImageView gambar;
        public ViewHolder(View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.nama_mobil);
            kategori = (TextView) itemView.findViewById(R.id.nama_kategori);
            harga = (TextView) itemView.findViewById(R.id.harga);
            gambar = (ImageView) itemView.findViewById(R.id.imgMobil);
            konfirmasi = (RelativeLayout)itemView.findViewById(R.id.konfirmasi);
        }


    }
}