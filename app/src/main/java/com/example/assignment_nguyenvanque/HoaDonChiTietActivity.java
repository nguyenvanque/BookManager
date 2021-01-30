package com.example.assignment_nguyenvanque;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.assignment_nguyenvanque.DAO.HoaDonChiTietDAO;
import com.example.assignment_nguyenvanque.DAO.SachDAO;
import com.example.assignment_nguyenvanque.adpater.CartAdapter;
import com.example.assignment_nguyenvanque.model.HoaDon;
import com.example.assignment_nguyenvanque.model.HoaDonChiTiet;
import com.example.assignment_nguyenvanque.model.Sach;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HoaDonChiTietActivity extends AppCompatActivity {
    EditText edMaHoaDon, edSoLuong;
    TextView tvThanhTien;
    Spinner spinnerMaSach;
    String maSach = "";
    String ngayHd = "";
    public static ArrayAdapter<Sach> dataAdapter;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    SachDAO sachFireBase;
    ArrayList<HoaDonChiTiet> list = new ArrayList<>();
    ArrayList<Sach> listSach;
    SwipeMenuListView lvCart;
    public static CartAdapter adapter = null;
    double thanhTien = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("CHI TIẾT HOÁ ĐƠN");
        setContentView(R.layout.activity_hoa_don_chi_tiet);
        init();
        setSpinner();
        adapter = new CartAdapter(this, list);
        lvCart.setAdapter(adapter);

        //Lấy mã hóa đơn từ Thêm hóa đơn
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            edMaHoaDon.setText(b.getString("MAHOADON"));
            ngayHd = b.getString("Ngay");
        }
        //Chọn mã sách trong spiner
        spinnerMaSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                maSach = listSach.get(spinnerMaSach.getSelectedItemPosition()).getMaSach();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Thanh Swipe để xóa
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_add);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        lvCart.setMenuCreator(creator);

        lvCart.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final HoaDonChiTiet hd = list.get(position);
                if (index == 0) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(HoaDonChiTietActivity.this);
                    builder2.setTitle("Cảnh báo");
                    builder2.setMessage("Bạn chắc chắn muốn xóa?");
                    builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hoaDonChiTietDAO.delete(hd.getMaHDCT());
                            list.clear();
                            list.addAll(hoaDonChiTietDAO.getAll());
                        }
                    });
                    builder2.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog dialog = builder2.create();
                    dialog.show();

                }
                return false;
            }
        });
    }

    private void init() {
        edMaHoaDon = (EditText) findViewById(R.id.edMaHoaDon);
        edMaHoaDon.setFocusable(false);
        edSoLuong = (EditText) findViewById(R.id.edSoLuongMua);
        spinnerMaSach = findViewById(R.id.spMaSach);
        lvCart = findViewById(R.id.lvCart);
        tvThanhTien = (TextView) findViewById(R.id.tvThanhTien);
    }


    public void ADDHoaDonCHITIET(View view) {
        hoaDonChiTietDAO = new HoaDonChiTietDAO(HoaDonChiTietActivity.this);
        sachFireBase = new SachDAO(HoaDonChiTietActivity.this);
        try {
            int sl = 0;
            //Sach
            Sach sach = null;
            int posision = 0;
            //Get list by mã sách
            for (int i = 0; i < listSach.size(); i++) {
                if (listSach.get(i).getMaSach().matches(maSach)) {
                    sach = listSach.get(i);
                    break;
                }
            }
            int check = checkListMaSach(list, maSach);
            HoaDon hoaDon = new HoaDon(edMaHoaDon.getText().toString(), ngayHd);

            //Hóa đơn chi tiết //edMaHoaDon.getText().toString()
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet("1", hoaDon, sach, Integer.parseInt(edSoLuong.getText().toString()));

            if (check >= 0) {
                int soluong = list.get(check).getSoLuongMua();
                hoaDonChiTiet.setSoLuongMua(soluong +
                        Integer.parseInt(edSoLuong.getText().toString()));
                list.set(check, hoaDonChiTiet);
                adapter.notifyDataSetChanged();
            } else {
                list.add(hoaDonChiTiet);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }
    public int checkListMaSach(ArrayList<HoaDonChiTiet> lsHD, String maSach) {
        int pos = -1;
        for (int i = 0; i < lsHD.size(); i++) {
            HoaDonChiTiet hd = lsHD.get(i);
            if (hd.getSach().getMaSach().equalsIgnoreCase(maSach)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    //        return pos;
//    }
    public void thanhToanHoaDon(View view) {
        hoaDonChiTietDAO = new HoaDonChiTietDAO(HoaDonChiTietActivity.this);
//tinh tien
        thanhTien = 0;
        try {
            for (HoaDonChiTiet hd : list) {
                hoaDonChiTietDAO.insert(hd);
                thanhTien = thanhTien + hd.getSoLuongMua() *
                        hd.getSach().getGiaBia();
            }
            tvThanhTien.setText("Tổng tiền: " + thanhTien);
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int checkMaSach(String strTheLoai) {
        for (int i = 0; i < listSach.size(); i++) {
            if (strTheLoai.equals(listSach.get(i).getMaSach())) {
                return i;
            }
        }
        return 0;
    }

    private void setSpinner() {
        //Đổ spinner
        //Khai báo hàm
        sachFireBase = new SachDAO(HoaDonChiTietActivity.this);
        listSach = sachFireBase.getAllS();
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSach);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaSach.setAdapter(dataAdapter);
    }
}
