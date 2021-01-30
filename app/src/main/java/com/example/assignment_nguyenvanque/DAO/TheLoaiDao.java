package com.example.assignment_nguyenvanque.DAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_nguyenvanque.ListSachActivity;
import com.example.assignment_nguyenvanque.ListTheLoaiActivity;
import com.example.assignment_nguyenvanque.model.TheLoai;
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
import java.util.List;

public class TheLoaiDao {
    DatabaseReference ref;
    Context context;

    ArrayList<TheLoai> listTheLoai;
    String sachId;

    public TheLoaiDao(Context context) {
        this.context = context;
        ref = FirebaseDatabase.getInstance().getReference("TheLoai");
    }

    public ArrayList<TheLoai> getAll() {
        listTheLoai = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTheLoai.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    TheLoai theLoai=ds.getValue(TheLoai.class);
                    listTheLoai.add(theLoai);
                }
                ListTheLoaiActivity.theLoaiAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listTheLoai;
    }
    public  ArrayList<TheLoai> getAllS() {
        listTheLoai = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    TheLoai theLoai = ds.getValue(TheLoai.class);
                    listTheLoai.add(theLoai);
                    ListSachActivity.adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listTheLoai;
    }
    public void insertS(TheLoai s) {
        sachId = ref.push().getKey();
        ref.child(sachId).setValue(s)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ListTheLoaiActivity.theLoaiAdapter.notifyDataSetChanged();
                        Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void update(final TheLoai s) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maTheLoai").getValue(String.class).equalsIgnoreCase(s.getMaTheLoai())){
                        sachId = data.getKey();
                        ref.child(sachId).setValue(s)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
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
    public void delete(final TheLoai s) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maTheLoai").getValue(String.class).equalsIgnoreCase(s.getMaTheLoai())){
                        sachId = data.getKey();
                        ref.child(sachId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ListTheLoaiActivity.theLoaiAdapter.notifyDataSetChanged();
                                        Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
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
}
