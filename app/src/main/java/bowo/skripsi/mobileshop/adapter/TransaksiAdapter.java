package bowo.skripsi.mobileshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import bowo.skripsi.mobileshop.DetailMobilAct;
import bowo.skripsi.mobileshop.LoginAct;
import bowo.skripsi.mobileshop.MainActivity;
import bowo.skripsi.mobileshop.R;
import bowo.skripsi.mobileshop.model.MobilModel;
import bowo.skripsi.mobileshop.model.TransaksiModel;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {
    Context context;
    private ArrayList<TransaksiModel> item;
   AdapterCallback callback;

    public interface AdapterCallback{
        void onItemClicked(int position);
    }
    public TransaksiAdapter(Context context, ArrayList<TransaksiModel> item, AdapterCallback callback) {
        this.callback = callback;
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi, parent, false); //memanggil layout list recyclerview
        ViewHolder processHolder = new ViewHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TransaksiModel data = item.get(position);
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
                openWhatsApp(v,data.getNama_mobil());
            }
        });



    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,konfirmasi,kategori,harga;
        ImageView gambar;
        public ViewHolder(View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.nama_mobil);
            kategori = (TextView) itemView.findViewById(R.id.nama_kategori);
            harga = (TextView) itemView.findViewById(R.id.harga_mobil);
            gambar = (ImageView) itemView.findViewById(R.id.imgMobil);
            konfirmasi = (TextView)itemView.findViewById(R.id.konfirmasi);
        }


    }
    public void openWhatsApp(View view,String body){
        PackageManager pm=context.getPackageManager();
        try {

            String teks = "Saya tertarik dengan mobil *"+body+"* dan ingin membelinya.";
            String toNumber = "628121559535";
            ;

            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                   toNumber, teks))));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Anda Tidak Memiliki Whatsapp",Toast.LENGTH_LONG).show();

        }
    }
}