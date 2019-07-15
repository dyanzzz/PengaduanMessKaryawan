package com.home.pengaduanmesskaryawan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.pengaduanmesskaryawan.R;
import com.home.pengaduanmesskaryawan.model.ModelTaskKamar;

import java.util.List;

public class TaskAdapterKamar extends RecyclerView.Adapter<TaskAdapterKamar.MyViewHolder> {
    private List<ModelTaskKamar> list;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomor, dataKamar;

        MyViewHolder(View view) {
            super(view);
            nomor       = view.findViewById(R.id.nomor);
            dataKamar   = view.findViewById(R.id.dataKamar);
        }
    }

    public TaskAdapterKamar(List<ModelTaskKamar> moviesList) {
        this.list = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewkamar, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModelTaskKamar modalTaskMasjid = list.get(position);
        String nomor        = modalTaskMasjid.getNomor();
        String username     = modalTaskMasjid.getUsername();
        String blokKamar    = modalTaskMasjid.getBlokKamar();
        String noKamar      = modalTaskMasjid.getNoKamar();
        String kdUser       = modalTaskMasjid.getKdUser();
        String kdKamar      = modalTaskMasjid.getKdKamar();
        String nama         = modalTaskMasjid.getNama();
        holder.nomor.setText(nomor);
        String dataKamar = "Blok " + blokKamar +" | " + "No. " + noKamar + " | " + nama;
        holder.dataKamar.setText(dataKamar);
        //holder.noKamar.setText(modalTaskMasjid.getNoKamar());
        //holder.nama.setText(modalTaskMasjid.getNama());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
