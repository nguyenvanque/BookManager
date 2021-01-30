package com.example.assignment_nguyenvanque;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.assignment_nguyenvanque.DAO.HoaDonDAO;
import com.example.assignment_nguyenvanque.model.HoaDon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HoaDonActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    EditText edNgayMua, edMaHoaDon;
    HoaDonDAO hoaDonDAO;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        setTitle("THÊM HOÁ ĐƠN");
        setContentView(R.layout.activity_hoa_don);
        edNgayMua  =findViewById(R.id.edNgayMua);
        edMaHoaDon =findViewById(R.id.edMaHoaDon);
    }


    public void datePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String ngay = dayOfMonth + "/" + (month + 1) + "/" + year;
                edNgayMua.setText(ngay);
            }
        }, y, m, d);
        datePickerDialog.show();
    }

    public void ADDHoaDon(View view) {
        hoaDonDAO = new HoaDonDAO(HoaDonActivity.this);
        try {
            if (validation() < 0) {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                HoaDon hoaDon = new
                        HoaDon(edMaHoaDon.getText().toString(), edNgayMua.getText().toString());
                if (hoaDonDAO.insert(hoaDon)) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new
                            Intent(HoaDonActivity.this, HoaDonChiTietActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("MAHOADON", edMaHoaDon.getText().toString());
                    bundle.putString("Ngay", edNgayMua.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Thêm thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int validation() {
        if
        (edMaHoaDon.getText().toString().isEmpty() || edNgayMua.getText().toString().isEmpty()
        ) {
            return -1;
        }
        return 1;
    }
}
