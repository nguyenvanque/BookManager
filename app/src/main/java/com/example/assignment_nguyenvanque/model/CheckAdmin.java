package com.example.assignment_nguyenvanque.model;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CheckAdmin {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    boolean checkAdmin=false;
    public void checkAdmin(){
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Dựa vào key đã put lên lấy data rồi đổ vào các view
                    String uid = "" + ds.child("uid").getValue();
                    String accountType = "" + ds.child("accountType").getValue();
                    if(accountType.equals("Admin")){
                       checkAdmin =true;
                    }
                    if(accountType.equals("User")){
                        checkAdmin =false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
