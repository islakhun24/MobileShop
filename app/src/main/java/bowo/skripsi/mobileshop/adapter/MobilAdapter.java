package bowo.skripsi.mobileshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bowo.skripsi.mobileshop.DetailMobilAct;
import bowo.skripsi.mobileshop.HomeAct;
import bowo.skripsi.mobileshop.LoginAct;
import bowo.skripsi.mobileshop.R;
import bowo.skripsi.mobileshop.fragmen.ProfileFrag;
import bowo.skripsi.mobileshop.model.KategoriModel;
import bowo.skripsi.mobileshop.model.MobilModel;

import static bowo.skripsi.mobileshop.HomeAct.my_shared_preferences;
import static bowo.skripsi.mobileshop.HomeAct.session_status;

public class MobilAdapter extends RecyclerView.Adapter<MobilAdapter.ViewHolder> implements Filterable {
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    Context context;
    private ArrayList<MobilModel> item;
    private ArrayList<MobilModel> itemFilter;
   AdapterCallback callback;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemFilter = item;
                } else {
                    ArrayList<MobilModel> filteredList = new ArrayList<>();
                    for (MobilModel row : item) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama_mobil().toLowerCase().contains(charString.toLowerCase()) || row.getMerk_mobil().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itemFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemFilter = (ArrayList<MobilModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterCallback{
        void onItemClicked(int position);
    }
    public MobilAdapter(Context context, ArrayList<MobilModel> item, AdapterCallback callback) {
        this.callback = callback;
        this.context = context;
        this.item = item;
        this.itemFilter = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobil, parent, false); //memanggil layout list recyclerview
        ViewHolder processHolder = new ViewHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        sharedpreferences = context.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        final MobilModel data = itemFilter.get(position);
        holder.nama.setText(data.getNama_mobil());
        holder.unit.setText(data.getStok_mobil()+" Unit");
        holder.tahun.setText(data.getTahun_mobil());
        holder.kategori.setText(data.getMerk_mobil()+" (" +data.getStatus_mobil()+")");
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        final int harga_mobil = Integer.parseInt(data.getHarga_mobil());

        holder.harga.setText(formatRupiah.format((double)harga_mobil));
        Picasso.with(context).load(API.GAMBAR_URL+data.getGambar_mobil()).placeholder(R.drawable.not_available).into(holder.gambar);
        //menampilkan data
        holder.rl1.setOnClickListener(new View.OnClickListener() {
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
                context.startActivities(new Intent[]{i});
            }
        });
        holder.beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session) {
                    callback.onItemClicked(Integer.parseInt(data.getId_mobil()));
                }else {
                    Intent intent = new Intent(context, LoginAct.class);
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemFilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,unit,tahun,kategori,harga;
        ImageView gambar;
        RelativeLayout rl1;
        Button beli;
        public ViewHolder(View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.nama_mobil);
            unit = (TextView) itemView.findViewById(R.id.jumlahUnit);
            tahun = (TextView) itemView.findViewById(R.id.tahun);
            kategori = (TextView) itemView.findViewById(R.id.nama_kategori);
            harga = (TextView) itemView.findViewById(R.id.harga_mobil);
            gambar = (ImageView) itemView.findViewById(R.id.imgMobil);
            rl1 = (RelativeLayout)itemView.findViewById(R.id.rl1);
            beli = (Button)itemView.findViewById(R.id.btn_beli);
        }


    }
}