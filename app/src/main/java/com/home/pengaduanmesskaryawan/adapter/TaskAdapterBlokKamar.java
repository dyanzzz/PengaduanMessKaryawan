package com.home.pengaduanmesskaryawan.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.home.pengaduanmesskaryawan.R;
import com.home.pengaduanmesskaryawan.model.ModelTaskBlokKamar;

import java.util.List;

public class TaskAdapterBlokKamar extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelTaskBlokKamar> modelItems;

    public TaskAdapterBlokKamar(Activity activity, List<ModelTaskBlokKamar> modelItems) {
        this.activity = activity;
        this.modelItems = modelItems;
    }

    @Override
    public int getCount() {
        return modelItems.size();
    }

    @Override
    public Object getItem(int location) {
        return modelItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.list_item_blok_kamar, null);
        }

        TextView nomor  = convertView.findViewById(R.id.nomor);
        TextView blokKamar  = convertView.findViewById(R.id.blokKamar);

        ModelTaskBlokKamar ambil = modelItems.get(position);

        nomor.setText(ambil.getNomor()+". ");
        blokKamar.setText("Blok "+ambil.getBlokKamar());

        return convertView;
    }


}
