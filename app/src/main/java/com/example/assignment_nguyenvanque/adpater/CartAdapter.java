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

import java.util.List;

public class CartAdapter extends BaseAdapter {
    List<HoaDonChiTiet> arrHoaDonChiTiet;
    public Activity context;
    public LayoutInflater inflater;


    public CartAdapter(Activity context, List<HoaDonChiTiet> arrayHoaDonChiTiet) {
        super();
        this.context = context;
        this.arrHoaDonChiTiet = arrayHoaDonChiTiet;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return arrHoaDonChiTiet.size();
    }
    @Override
    public Object getItem(int position) {
        return arrHoaDonChiTiet.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class ViewHolder {
        TextView txtMaSach;
        TextView txtSoLuong;
        TextView txtGiaBia;
        TextView txtThanhTien;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cart, null);
            holder.txtMaSach = convertView.findViewById(R.id.tvMaSach);
            holder.txtSoLuong =  convertView.findViewById(R.id.tvSoLuong);
            holder.txtGiaBia =  convertView.findViewById(R.id.tvGiaBia);
            holder.txtThanhTien = (TextView)
                    convertView.findViewById(R.id.tvThanhTien);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        HoaDonChiTiet hoaDonChiTiet =  arrHoaDonChiTiet.get(position);
        holder.txtMaSach.setText("Mã sách: " + hoaDonChiTiet.getSach().getMaSach());
        holder.txtSoLuong.setText("Số lượng: " + hoaDonChiTiet.getSoLuongMua());
        holder.txtGiaBia.setText("Giá bìa: " + hoaDonChiTiet.getSach().getGiaBia() + " VND");
        holder.txtThanhTien.setText("Thành tiền: " + hoaDonChiTiet.getSoLuongMua() * hoaDonChiTiet.getSach().getGiaBia() + " VND");
        return convertView;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public void changeDataset(List<HoaDonChiTiet> items) {
        this.arrHoaDonChiTiet = items;
        notifyDataSetChanged();
    }
}
