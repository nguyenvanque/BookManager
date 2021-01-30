package com.example.assignment_nguyenvanque.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.assignment_nguyenvanque.ThongKeActivity;
import com.example.assignment_nguyenvanque.model.HoaDonChiTiet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class HoaDonChiTietDAO {
    Context context;
    DatabaseReference reference;
    String key = "";
    ArrayList<HoaDonChiTiet> list = new ArrayList<>();

    public HoaDonChiTietDAO() {
    }

    public HoaDonChiTietDAO(Context context) {
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("HoaDonChiTiet");
    }

    public ArrayList<HoaDonChiTiet> getAll() {
        final ArrayList<HoaDonChiTiet> list = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        HoaDonChiTiet hoaDon = next.getValue(HoaDonChiTiet.class);
                        list.add(hoaDon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return list;
    }

    public boolean insert(HoaDonChiTiet hoaDon) {
        reference.push().setValue(hoaDon).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        return true;
    }

    public boolean update(final HoaDonChiTiet hoaDon) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maHoaDon").getValue(String.class).equalsIgnoreCase(hoaDon.getMaHDCT())) {
                        key = data.getKey();
                        reference.child(key).setValue(hoaDon);
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return true;
    }
    public void delete(final String maHoaDon) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maHoaDon").getValue(String.class).equalsIgnoreCase(maHoaDon)) {
                        key = data.getKey();
                        reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void getAllHD() {
        ThongKeActivity.listHDCT.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ThongKeActivity.listHDCT.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        HoaDonChiTiet hoaDon = next.getValue(HoaDonChiTiet.class);
                        list.add(hoaDon);
                    }
                    ThongKeActivity.listHDCT.clear();
                    ThongKeActivity.listHDCT.addAll(list);
                    ThongKeActivity.showDthu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.i("hdct3", "" + list.size());
    }
}
