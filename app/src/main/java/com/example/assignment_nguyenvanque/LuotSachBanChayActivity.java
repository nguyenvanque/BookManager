package com.example.assignment_nguyenvanque;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.assignment_nguyenvanque.DAO.HoaDonChiTietDAO;
import com.example.assignment_nguyenvanque.DAO.SachDAO;
import com.example.assignment_nguyenvanque.adpater.SachAdapter;
import com.example.assignment_nguyenvanque.model.HoaDonChiTiet;
import com.example.assignment_nguyenvanque.model.Sach;

import java.util.ArrayList;
import java.util.List;

public class LuotSachBanChayActivity extends AppCompatActivity {
    public static List<Sach> dsSach = new ArrayList<>();
    RecyclerView recyclerView;
    SachAdapter adapter = null;
    EditText edThang;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    SachDAO sachFireBase;
    ArrayList<HoaDonChiTiet> listHDCT;
    ArrayList<HoaDonChiTiet> listTheoThang = new ArrayList<>();
    ArrayList<Sach> list = new ArrayList<>();
    Button btnTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("TOP SÁCH BÁN CHẠY");
        setContentView(R.layout.activity_luot_sach_ban_chay);
        recyclerView =  findViewById(R.id.lvBookTop);
        btnTim=findViewById(R.id.btnTim);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(LuotSachBanChayActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        edThang =  findViewById(R.id.edThang);
        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);
        sachFireBase = new SachDAO(this);
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thang = edThang.getText().toString();
                if (Integer.parseInt(thang) > 13 ||
                        Integer.parseInt(thang) < 0) {
                    Toast.makeText(getApplicationContext(), "Không đúng định dạng tháng (1-12)", Toast.LENGTH_SHORT).show();
                } else {
                    //Lọc list theo tháng
                    getListByMonth(thang);
                    //Lọc top 10 rồi show lên
                    top10();
                    convertMaSach();
                    adapter = new SachAdapter(LuotSachBanChayActivity.this, list);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        listHDCT = hoaDonChiTietDAO.getAll();
        dsSach = sachFireBase.getAllNone();
    }
    public void getListByMonth(String month) {
        for (int i = 0; i < listHDCT.size(); i++) {
            String date = listHDCT.get(i).getHoaDon().getNgayMua();
            String thang = date.substring(date.length()-6,date.length()-4);
            if(thang.substring(1,2).matches("/")){
                thang = thang.substring(0,1);
            }
            if (thang.matches(month)) {
                listTheoThang.add(listHDCT.get(i));
            }
        }
    }
    //Lọc sách bán hạy{ nhất
    public void top10() {
        if (listTheoThang.size() > 10) {
            for (int i = 0; i < listTheoThang.size(); i++) {
                int sl1 = listTheoThang.get(i).getSoLuongMua();
                for (int j = i + 1; j < listTheoThang.size() - 1; j++) {
                    int sl2 = listTheoThang.get(j).getSoLuongMua();
                    if (sl1 < sl2) {
                        HoaDonChiTiet l = listTheoThang.get(i);
                        listTheoThang.set(i, listTheoThang.get(j));
                        listTheoThang.set(j, l);
                    }
                }
            }
        }
    }


    public void convertMaSach() {
        for (int i = 0; i < dsSach.size(); i++) {
            String ma = listTheoThang.get(i).getSach().getMaSach();
            if (dsSach.get(i).getMaSach().matches(ma)) {
                list.add(dsSach.get(i));
            }
        }
    }
}
