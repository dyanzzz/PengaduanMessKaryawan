package com.home.pengaduanmesskaryawan.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.pengaduanmesskaryawan.R;
import com.home.pengaduanmesskaryawan.model.ModelTaskPengaduan;

import java.util.List;

public class TaskAdapterPengaduan extends RecyclerView.Adapter<TaskAdapterPengaduan.MyViewHolder> {

    private List<ModelTaskPengaduan> list;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomor, nama, statusKeluhan, tanggalKeluhan, dataKeluhan;

        MyViewHolder(View view) {
            super(view);
            nomor           = view.findViewById(R.id.nomor);
            nama            = view.findViewById(R.id.nama);
            statusKeluhan   = view.findViewById(R.id.statusKeluhan);
            tanggalKeluhan  = view.findViewById(R.id.tanggalKeluhan);
            dataKeluhan     = view.findViewById(R.id.dataKeluhan);
        }
    }

    public TaskAdapterPengaduan(List<ModelTaskPengaduan> moviesList) {
        this.list = moviesList;
    }

    @Override
    public TaskAdapterPengaduan.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewpengaduan, parent, false);

        return new TaskAdapterPengaduan.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskAdapterPengaduan.MyViewHolder holder, int position) {
        ModelTaskPengaduan modalTaskMasjid = list.get(position);
        String nomor            = modalTaskMasjid.getNomor();
        String blokKamar        = modalTaskMasjid.getBlokKamar();
        String noKamar          = modalTaskMasjid.getNoKamar();
        String kdUser           = modalTaskMasjid.getKdUser();
        String kdKamar          = modalTaskMasjid.getKdKamar();
        String kdKeluhan        = modalTaskMasjid.getKdKeluhan();
        String nama             = modalTaskMasjid.getNama();
        String keluhan          = modalTaskMasjid.getKeluhan();
        String tanggalKeluhan   = modalTaskMasjid.getTanggalKeluhan();
        String statusKeluhan    = modalTaskMasjid.getStatusKeluhan();
        String status;

        holder.nomor.setText(nomor);
        String dataKamar    = "Blok " + blokKamar +" | " + "No. " + noKamar + " | " + nama;

        switch (statusKeluhan) {
            case "Menunggu": {
                status = "Status keluhan : <font color='#FF0000'>" + statusKeluhan + "</font>";
                break;
            }
            case "Proses": {
                status = "Status keluhan : <font color='#00FFFF'>" + statusKeluhan + "</font>";
                break;
            }
            default: {
                status = "Status keluhan : <font color='#00FF00'>" + statusKeluhan + "</font>";
                break;
            }
        }


        holder.nama.setText(dataKamar);
        holder.statusKeluhan.setText(Html.fromHtml(status));
        holder.tanggalKeluhan.setText(tanggalKeluhan);
        holder.dataKeluhan.setText(keluhan);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
