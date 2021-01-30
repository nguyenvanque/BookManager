package com.example.assignment_nguyenvanque.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment_nguyenvanque.R;
import com.example.assignment_nguyenvanque.adpater.AdapterUser;
import com.example.assignment_nguyenvanque.model.NguoiDung;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    AdapterUser adapterUser;
    ArrayList<NguoiDung> userList;
    private FirebaseAuth firebaseAuth;
    public UsersFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        userList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllUser();
        return view;
    }
    private void getAllUser() {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    NguoiDung modelUser = ds.getValue(NguoiDung.class);
                    if (!modelUser.getUid().equals(fuser.getUid())) {
                        userList.add(modelUser);
                    }
                    adapterUser = new AdapterUser(getActivity(), userList);
                    recyclerView.setAdapter(adapterUser);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
