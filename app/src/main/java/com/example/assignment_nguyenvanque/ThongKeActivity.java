package com.example.assignment_nguyenvanque;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.assignment_nguyenvanque.DAO.HoaDonChiTietDAO;
import com.example.assignment_nguyenvanque.model.HoaDonChiTiet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThongKeActivity extends AppCompatActivity {
    public static TextView tvNgay, tvThang, tvNam;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    public static ArrayList<HoaDonChiTiet> listHDCT = new ArrayList<>();
    public static String ngay, thang, nam;
    public static int tongNgay = 0, tongThang = 0, tongNam = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        setTitle("THỐNG KÊ");
        hoaDonChiTietDAO=new HoaDonChiTietDAO(ThongKeActivity.this);
        tongNgay = 0;
        tongThang = 0;
        tongNam = 0;
        tvNgay = findViewById(R.id.tvThongKeNgay);
        tvThang = findViewById(R.id.tvThongKeThang);
        tvNam =  findViewById(R.id.tvThongKeNam);

        tvNgay.setText(tongNgay+" vnđ");
        tvThang.setText(tongThang+" vnđ");
        tvNam.setText(tongNam+" vnđ");
        getDateNow();
        hoaDonChiTietDAO.getAllHD();
    }
    public static void getDateNow() {
        //Lấy ngày hiện tại
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ngay = currentDate.substring(0, 2);
        thang = currentDate.substring(3, 5);
        nam = currentDate.substring(6, currentDate.length());
        Log.d("ngay", ngay+ thang+nam);
    }
    //SHow doanh thu theo ngày, tháng, năm
    public static void showDthu() {
        for (int i = 0; i < listHDCT.size(); i++) {
            String ngayTK = listHDCT.get(i).getHoaDon().getNgayMua().substring(0, 2);
            String thangTK = listHDCT.get(i).getHoaDon().getNgayMua().substring(3, 5);
            String namTK = listHDCT.get(i).getHoaDon().getNgayMua().substring(6, listHDCT.get(i).getHoaDon().getNgayMua().length());
            if (ngay.matches(ngayTK)) {
                tongNgay += listHDCT.get(i).getSoLuongMua() * listHDCT.get(i).getSach().getGiaBia();
            }
            if (thang.matches(thangTK)) {
                tongThang += listHDCT.get(i).getSoLuongMua() * listHDCT.get(i).getSach().getGiaBia();
            }
            if (nam.matches(namTK)) {
                tongNam += listHDCT.get(i).getSoLuongMua() * listHDCT.get(i).getSach().getGiaBia();
            }
        }
        tvNgay.setText(tongNgay+" vnđ");
        tvThang.setText(tongThang+" vnđ");
        tvNam.setText(tongNam+" vnđ");
    }
}
