package com.example.assignment_nguyenvanque.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_nguyenvanque.R;
import com.example.assignment_nguyenvanque.model.NguoiDung;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterUser extends  RecyclerView.Adapter<AdapterUser.MyHoler>{
 Context context;
 ArrayList<NguoiDung> userList;

    public AdapterUser(Context context, ArrayList<NguoiDung> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.row_user,parent,false);

        return new MyHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHoler holder, int position) {
        final String hisUid=userList.get(position).getUid();
           String userImage      =userList.get(position).getImage();
           String userName       =userList.get(position).getName();
           final String userEmail=userList.get(position).getEmail();

           holder.mNameTv.setText(userName);
           holder.mEmailTv.setText(userEmail);
           try {
               Picasso.with(context).load(userImage).placeholder(R.drawable.ic_face).into(holder.mAvatarIv);
           }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class  MyHoler extends RecyclerView.ViewHolder{
      ImageView mAvatarIv;
      TextView mNameTv,mEmailTv;
        public MyHoler(@NonNull View itemView) {
            super(itemView);
            mAvatarIv=itemView.findViewById(R.id.avatarIv);
            mNameTv=itemView.findViewById(R.id.nameTv);
            mEmailTv=itemView.findViewById(R.id.emailTvrow);
        }
    }
}
