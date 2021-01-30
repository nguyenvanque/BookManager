package com.example.assignment_nguyenvanque;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.assignment_nguyenvanque.DAO.HoaDonChiTietDAO;
import com.example.assignment_nguyenvanque.DAO.HoaDonDAO;
import com.example.assignment_nguyenvanque.adpater.HoaDonAdapter;
import com.example.assignment_nguyenvanque.adpater.One_sach_HDCT;
import com.example.assignment_nguyenvanque.model.HoaDon;
import com.example.assignment_nguyenvanque.model.HoaDonChiTiet;
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
import java.util.List;

public class ListHoaDonActivity extends AppCompatActivity {
    public List<HoaDon> list = new ArrayList<>();
    SwipeMenuListView lvHoaDon;
    public static HoaDonAdapter adapter = null;
    //    HoaDonDAO hoaDonDAO;
    HoaDonDAO hoaDonFireBase;
    HoaDonChiTietDAO hoaDonChiTietFireBase;
    ArrayList<HoaDonChiTiet> listHDCT;

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hoa_don);
        setTitle("HOÁ ĐƠN");
        lvHoaDon = findViewById(R.id.lvHoaDon);
        btnAdd = findViewById(R.id.btn_Add);
        hoaDonFireBase = new HoaDonDAO(ListHoaDonActivity.this);
        hoaDonChiTietFireBase = new HoaDonChiTietDAO(this);
        listHDCT = hoaDonChiTietFireBase.getAll();

        list = hoaDonFireBase.getAll();
        adapter = new HoaDonAdapter(this, list);
        lvHoaDon.setAdapter(adapter);
        reference= FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new
                        Intent(ListHoaDonActivity.this, HoaDonActivity.class);
                startActivity(intent);
            }
        });

        //Khi click vào từng hóa đơn sẽ show ra hóa đơn chi tiết
        lvHoaDon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String maHD = list.get(position).getMaHoaDon();
                ArrayList<HoaDonChiTiet> showHDCT = new ArrayList<>();

                //Lọc list theo đúng hóa đơn

                for (int i = 0; i < listHDCT.size(); i++) {
                    String ma = listHDCT.get(i).getHoaDon().getMaHoaDon();
                    if (maHD.matches(ma)) {
                        showHDCT.add(listHDCT.get(i));
                    }
                }
                Toast.makeText(ListHoaDonActivity.this, showHDCT.size() + "", Toast.LENGTH_SHORT).show();

                //Hiện dialog
                Dialog dialog=new Dialog(ListHoaDonActivity.this);
                dialog.setContentView(R.layout.show_hd_chi_tiet);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final TextView ngay, ma, khachHang, sdt, tongTien,txtemail;
                ListView listView;
                ngay = dialog.findViewById(R.id.showNgayHDCT);
                ma = dialog.findViewById(R.id.showMaHDCT);
                khachHang = dialog.findViewById(R.id.showTenHDCT);
                sdt = dialog.findViewById(R.id.showSdtHDCT);
                txtemail = dialog.findViewById(R.id.showEmail);
                tongTien = dialog.findViewById(R.id.tongTienHDCT);
                listView = dialog.findViewById(R.id.lvShowHDCT);

                ngay.setText(showHDCT.get(0).getHoaDon().getNgayMua());
                ma.setText(showHDCT.get(0).getHoaDon().getMaHoaDon());
                Query query = reference.orderByChild("email").equalTo(user.getEmail());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //lấy data đổ vào các view
                            String name = "" + ds.child("name").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String image = "" + ds.child("image").getValue();
                            String cover = "" + ds.child("cover").getValue();

                            khachHang.setText(name);
                            sdt.setText(phone);
                            txtemail.setText(email);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //Đổ adapter vào listView

                One_sach_HDCT adapter = new One_sach_HDCT(ListHoaDonActivity.this, showHDCT);
                listView.setAdapter(adapter);

                //Tính tổng tiền theo số lượng sách
                int tong = 0;
                for (int i = 0; i < showHDCT.size(); i++) {
                    tong += (showHDCT.get(i).getSach().getGiaBia()) * (showHDCT.get(i).getSoLuongMua());
                }
                tongTien.setText(tong+"");
                dialog.show();

            }
        });

        // TextFilter
        lvHoaDon.setTextFilterEnabled(true);
        EditText edSeach = (EditText) findViewById(R.id.edSearch);
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Thanh Swipe để xóa
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        lvHoaDon.setMenuCreator(creator);

        lvHoaDon.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final HoaDon hd = list.get(position);
                if (index == 0) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(ListHoaDonActivity.this);
                    builder2.setTitle("Cảnh báo");
                    builder2.setMessage("Bạn chắc chắn muốn xóa?");
                    builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hoaDonFireBase.delete(hd.getMaHoaDon());
                            list.clear();
                            list.addAll(hoaDonFireBase.getAll());
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


}
