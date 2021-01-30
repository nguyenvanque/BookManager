package com.example.assignment_nguyenvanque.adpater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.assignment_nguyenvanque.R;
import com.example.assignment_nguyenvanque.model.HoaDonChiTiet;

import java.util.ArrayList;

public class One_sach_HDCT extends BaseAdapter {
    Context context;
    ArrayList<HoaDonChiTiet> list;

    public One_sach_HDCT(Context context, ArrayList<HoaDonChiTiet> list) {
        this.context = context;
        this.list = list;
    }
    class ViewHolder {
        TextView ten, sl, gia;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        HoaDonChiTiet hd = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.one_sach_hdct, null);
            holder.ten = convertView.findViewById(R.id.lvTenSachHDCT);
            holder.sl = convertView.findViewById(R.id.lvSLSachHDCT);
            holder.gia = convertView.findViewById(R.id.lvGiaSachHDCT);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.ten.setText(hd.getSach().getTenSach());
        holder.gia.setText(hd.getSach().getGiaBia()+"");
        holder.sl.setText(hd.getSoLuongMua()+"");
        return convertView;
    }
}
