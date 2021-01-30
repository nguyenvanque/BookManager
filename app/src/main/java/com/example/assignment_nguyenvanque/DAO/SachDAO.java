package com.example.assignment_nguyenvanque.DAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_nguyenvanque.HoaDonChiTietActivity;
import com.example.assignment_nguyenvanque.ListSachActivity;
import com.example.assignment_nguyenvanque.model.Sach;
import com.example.assignment_nguyenvanque.model.TheLoai;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SachDAO {
    DatabaseReference ref;
    Context context;
    static ProgressDialog progressDialog;
    ArrayList<Sach> listSach;
    String NDId = null;
    public SachDAO(Context context) {
        this.context = context;
        ref = FirebaseDatabase.getInstance().getReference("Sach");
    }

    public ArrayList<Sach> getAll() {
        listSach = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSach.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Sach theLoai = ds.getValue(Sach.class);
                    listSach.add(theLoai);
                    ListSachActivity.sachAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listSach;
    }
    public ArrayList<Sach> getAllS() {
        listSach = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSach.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Sach theLoai = ds.getValue(Sach.class);
                    listSach.add(theLoai);
                    HoaDonChiTietActivity.dataAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listSach;
    }
    public  void insert(final AppCompatActivity activity,
                        final DatabaseReference ref,
                        final Sach sach) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Đang xử lý...");

        progressDialog.show();
        if (sach == null) {
            Toast.makeText(activity, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }
        ref.push().setValue(sach).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(activity, "Thành công", Toast.LENGTH_SHORT).show();
                ListSachActivity.sachAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


    public void update(final Sach nd){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("maSach").getValue(String.class).equalsIgnoreCase(nd.getMaSach())){
                        NDId = data.getKey();
                        ref.child(NDId).setValue(nd);
                        ListSachActivity.sachAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void delete(final String name){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("maSach").getValue(String.class).equalsIgnoreCase(name)){
                        NDId = data.getKey();
                        ref.child(NDId).removeValue();
                        ListSachActivity.sachAdapter.notifyDataSetChanged();                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<Sach> getAllNone() {
        final ArrayList<Sach> listSach = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Sach").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listSach.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        Sach sach = next.getValue(Sach.class);
                        listSach.add(sach);
                        Log.i("loi", listSach.get(0).getMaSach());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listSach;
    }
}
