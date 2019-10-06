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
import com.home.pengaduanmesskaryawan.model.ModelTaskUser;

import java.util.List;

public class TaskAdapterUser extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelTaskUser> modelItems;

    public TaskAdapterUser(Activity activity, List<ModelTaskUser> modelItems) {
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
            convertView = inflater.inflate(R.layout.list_item_user, null);
        }

        TextView nomor  = convertView.findViewById(R.id.nomor);
        TextView user  = convertView.findViewById(R.id.user);
        TextView kdUser  = convertView.findViewById(R.id.kdUser);
        TextView kdKamar  = convertView.findViewById(R.id.kdKamar);

        kdUser.setVisibility(View.GONE);
        kdKamar.setVisibility(View.GONE);

        ModelTaskUser ambil = modelItems.get(position);

        nomor.setText(ambil.getNomor()+". ");
        user.setText(ambil.getNama());
        kdUser.setText(ambil.getKdUser());
        kdKamar.setText(ambil.getKdKamar());

        return convertView;
    }

}
