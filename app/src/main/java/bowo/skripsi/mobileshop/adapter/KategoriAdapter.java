package bowo.skripsi.mobileshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bowo.skripsi.mobileshop.FilterKategoriAct;
import bowo.skripsi.mobileshop.R;
import bowo.skripsi.mobileshop.model.KategoriModel;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder> {

    Context context;
    private ArrayList<KategoriModel> item;

    public KategoriAdapter(Context context, ArrayList<KategoriModel> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false); //memanggil layout list recyclerview
        ViewHolder processHolder = new ViewHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final KategoriModel data = item.get(position);
        holder.nama.setText(data.getNama());//menampilkan data
        holder.lnKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilterKategoriAct.class);
                i.putExtra("merk_mobil",data.getNama());
                context.startActivities(new Intent[]{i});
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama;
        LinearLayout lnKategori;
        public ViewHolder(View itemView) {
            super(itemView);
            lnKategori = (LinearLayout) itemView.findViewById(R.id.lnKategori);
            nama = (TextView) itemView.findViewById(R.id.nama_kategori);

        }


    }
}