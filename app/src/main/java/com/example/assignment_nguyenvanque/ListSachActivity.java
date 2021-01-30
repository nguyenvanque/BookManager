package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.assignment_nguyenvanque.DAO.SachDAO;
import com.example.assignment_nguyenvanque.DAO.TheLoaiDao;
import com.example.assignment_nguyenvanque.adpater.SachAdapter;
import com.example.assignment_nguyenvanque.adpater.TheLoaiAdapter;
import com.example.assignment_nguyenvanque.model.Sach;
import com.example.assignment_nguyenvanque.model.TheLoai;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListSachActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton btn_AddSach;
    RelativeLayout relativeLayout;

    ArrayList<Sach> listSach;
    ArrayList<TheLoai> listTheLoai;

    public static SachAdapter sachAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseUser firebaseUser;

    SachDAO sachDAO;

    TheLoaiDao theLoaiDao;
    Dialog dialog;
    Spinner spMaTheLoai;
    public static ArrayAdapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sach);
        setTitle("DANH SÁCH SÁCH");
        recyclerView = findViewById(R.id.recyclerview_sach);
        btn_AddSach = findViewById(R.id.btn_themsach);
        relativeLayout = findViewById(R.id.realative_Sach);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Sach");
        Query query = FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .orderByChild("email")
                .equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Dựa vào key đã put lên lấy data rồi đổ vào các view
                    String uid = "" + ds.child("uid").getValue();
                    String accountType = "" + ds.child("accountType").getValue();
                    if(accountType.equals("Admin")){
                        btn_AddSach.setVisibility(View.VISIBLE);
                    }
                    if(accountType.equals("User")){
                        btn_AddSach.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (HomeActivity.isDark == true) {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }

        dialog = new Dialog(ListSachActivity.this);
        dialog.setContentView(R.layout.dialog_themsach);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        spMaTheLoai = dialog.findViewById(R.id.spnTheLoai);


        sachDAO = new SachDAO(ListSachActivity.this);
        theLoaiDao = new TheLoaiDao(ListSachActivity.this);


        listSach = new ArrayList<>();
        listTheLoai = new ArrayList<>();
        listTheLoai = theLoaiDao.getAllS();
        adapter = new ArrayAdapter(ListSachActivity.this, android.R.layout.simple_spinner_item, listTheLoai);
        spMaTheLoai.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ListSachActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        listSach = sachDAO.getAll();
        sachAdapter = new SachAdapter(ListSachActivity.this, listSach);
        recyclerView.setAdapter(sachAdapter);


        btn_AddSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ListSachActivity.this, listTheLoai.size() + "", Toast.LENGTH_SHORT).show();
                final EditText edt_MaSach = dialog.findViewById(R.id.edt_MaSach);
                final EditText edt_TenSach = dialog.findViewById(R.id.edt_TenSach);
                final EditText edt_TacGia = dialog.findViewById(R.id.edt_TacGia);
                final EditText edt_NXB = dialog.findViewById(R.id.edtt_NXB);
                final EditText edt_Soluong = dialog.findViewById(R.id.edt_SoLuong);
                final EditText edt_GiaBia = dialog.findViewById(R.id.edt_GiaBia);
                final Button btnThem = dialog.findViewById(R.id.btnAddBook);
                final Button btnHuy = dialog.findViewById(R.id.btnShowBook);

                btnThem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String maSach = edt_MaSach.getText().toString();
                        String tenSach = edt_TenSach.getText().toString();
                        String NXB = edt_NXB.getText().toString();
                        String tacGia = edt_TacGia.getText().toString();
                        String soluong = edt_Soluong.getText().toString();
                        String giaBia = edt_GiaBia.getText().toString();
                        TheLoai ls = (TheLoai) spMaTheLoai.getSelectedItem();
                        String maTheLoai = ls.getMaTheLoai();
                        if (maSach.isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Mã sách không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (tenSach.isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Tên sách không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (NXB.isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Nhà xuất bản không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (tacGia.isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Nhà xuất bản không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (tacGia.isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Nhà xuất bản không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (tacGia.isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Tác giả không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (edt_Soluong.getText().toString().isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Số lượng không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (edt_GiaBia.getText().toString().isEmpty()) {
                            Toast.makeText(ListSachActivity.this, "Giá bìa không được để trống", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(tenSach.matches((".*[0-9].*"))){
                            Toast.makeText(ListSachActivity.this, "Tên Sách phải là chữ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(NXB.matches((".*[0-9].*"))){
                            Toast.makeText(ListSachActivity.this, "Nhà xuất bản phải là chữ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(tacGia.matches((".*[0-9].*"))){
                            Toast.makeText(ListSachActivity.this, "Tác giả phải là chữ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(!soluong.matches((".*[0-9].*"))){
                            Toast.makeText(ListSachActivity.this, "Số lượng phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(!giaBia.matches((".*[0-9].*"))){
                            Toast.makeText(ListSachActivity.this, "Giá bìa phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                      else   if (xetTrung(maSach)) {
                            Toast.makeText(ListSachActivity.this, "Mã sách không được trùng", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Sach sach = new Sach(maSach, maTheLoai, tenSach, tacGia, NXB, Double.parseDouble(giaBia), Integer.parseInt(soluong));
                            listSach.clear();
                            sachDAO.insert(ListSachActivity.this, myRef, sach);
                            dialog.dismiss();
                        }


                    }

                });
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

    }
    public boolean xetTrung(String maTheLoai) {
        Boolean xet = false;
        for (int i = 0; i < listSach.size(); i++) {
            String ma = listSach.get(i).getMaTheLoai();
            if (ma.equalsIgnoreCase(maTheLoai)) {
                xet = true;
                break;
            }
        }
        return xet;
    }
}
