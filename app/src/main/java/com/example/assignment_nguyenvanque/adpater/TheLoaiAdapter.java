package com.example.assignment_nguyenvanque.adpater;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment_nguyenvanque.DAO.TheLoaiDao;
import com.example.assignment_nguyenvanque.ListTheLoaiActivity;
import com.example.assignment_nguyenvanque.LoginActivity;
import com.example.assignment_nguyenvanque.R;
import com.example.assignment_nguyenvanque.SuaTheLoaiActivity;
import com.example.assignment_nguyenvanque.model.TheLoai;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.ViewHolder>{

    Context context;
    ArrayList<TheLoai> listTheLoai;

    TheLoaiDao theLoaiDao;
    Animation animationItem;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public TheLoaiAdapter(Context context, ArrayList<TheLoai> list) {
        this.context = context;
        this.listTheLoai = list;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_theloai,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        theLoaiDao=new TheLoaiDao(context);
        final TheLoai theLoai=listTheLoai.get(position);
        holder.txtMaLoai.setText(theLoai.getMaTheLoai());
        holder.txtTenLoai.setText(theLoai.getTenTheLoai());
        if(theLoai.getHinhAnh().equals("")){
            holder.imgHinh.setImageResource(R.drawable.ic_face);
        }else {
            Glide.with(context).load(theLoai.getHinhAnh()).into(holder.imgHinh);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        context,R.style.BottomSheetDialogTheme
                );

                final View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.bottom_sheet_dialog,
                        (LinearLayout)bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
                Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(firebaseUser.getEmail());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //Dựa vào key đã put lên lấy data rồi đổ vào các view
                            String uid = "" + ds.child("uid").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            if(accountType.equals("Admin")){
                                bottomSheetView.findViewById(R.id.txt_SuaTheLoai).setVisibility(View.VISIBLE);
                                bottomSheetView.findViewById(R.id.txt_XoaKhoaHoc).setVisibility(View.VISIBLE);
                                bottomSheetView.findViewById(R.id.viewSua).setVisibility(View.VISIBLE);
                                bottomSheetView.findViewById(R.id.viewXoa).setVisibility(View.VISIBLE);                            }
                            if(accountType.equals("User")){
                                bottomSheetView.findViewById(R.id.txt_SuaTheLoai).setVisibility(View.GONE);
                                bottomSheetView.findViewById(R.id.txt_XoaKhoaHoc).setVisibility(View.GONE);
                                bottomSheetView.findViewById(R.id.viewSua).setVisibility(View.GONE);
                                bottomSheetView.findViewById(R.id.viewXoa).setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                if(LoginActivity.setView==true){
//                    bottomSheetView.findViewById(R.id.txt_SuaTheLoai).setVisibility(View.VISIBLE);
//                    bottomSheetView.findViewById(R.id.txt_XoaKhoaHoc).setVisibility(View.VISIBLE);
//                    bottomSheetView.findViewById(R.id.viewSua).setVisibility(View.VISIBLE);
//                    bottomSheetView.findViewById(R.id.viewXoa).setVisibility(View.VISIBLE);
//                }else {
//                    bottomSheetView.findViewById(R.id.txt_SuaTheLoai).setVisibility(View.GONE);
//                    bottomSheetView.findViewById(R.id.txt_XoaKhoaHoc).setVisibility(View.GONE);
//                    bottomSheetView.findViewById(R.id.viewSua).setVisibility(View.GONE);
//                    bottomSheetView.findViewById(R.id.viewXoa).setVisibility(View.GONE);
//                }

                bottomSheetView.findViewById(R.id.txt_XemChiTiet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                context,R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(context).inflate(
                                R.layout.dialog_chitiet_theloai,
                                (RelativeLayout)bottomSheetDialog.findViewById(R.id.realative_chitiet)
                        );
                        CircleImageView img=bottomSheetView.findViewById(R.id.details_image);
                        TextView txtMaTheLoai    =bottomSheetView.findViewById(R.id.txt_MaTheLoai);
                        TextView txtTenTheLoai   =bottomSheetView.findViewById(R.id.txt_TenTheLoai);
                        TextView txtMoTa   =bottomSheetView.findViewById(R.id.txt_MoTa);
                        TextView txtViTri  =bottomSheetView.findViewById(R.id.txt_ViTri);
                        Button btnHuy=bottomSheetView.findViewById(R.id.btn_HuyChiTiet);

                        txtMaTheLoai.setText(theLoai.getMaTheLoai());
                        txtTenTheLoai.setText(theLoai.getTenTheLoai());
                        txtMoTa.setText(theLoai.getMoTa());
                        txtViTri.setText(theLoai.getViTri()+"");
                        Log.d("hinh",theLoai.getHinhAnh().toString());

                        try {
                            if(theLoai.getHinhAnh()==""){
                                img.setImageResource(R.drawable.ic_face);
                            }else {
                                Picasso.with(context).load(theLoai.getHinhAnh()).placeholder(R.drawable.ic_face).into(img);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        btnHuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
                });

                bottomSheetView.findViewById(R.id.txt_SuaTheLoai).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, SuaTheLoaiActivity.class);
                        intent.putExtra("mybook",theLoai);
                        context.startActivity(intent);
                    }
                });

                bottomSheetView.findViewById(R.id.txt_XoaKhoaHoc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_xoa);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }

                        final TextView txt_Massage = dialog.findViewById(R.id.txt_Titleconfirm);
                        final Button btn_Yes = dialog.findViewById(R.id.btn_yes);
                        final Button btn_No = dialog.findViewById(R.id.btn_no);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);

                        progressBar.setVisibility(View.INVISIBLE);
                        final   TheLoai theLoai1=listTheLoai.get(position);
                        txt_Massage.setText("Bạn có muốn xóa thể loại " + listTheLoai.get(position).getTenTheLoai() + " hay không ? ");
                        btn_Yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                theLoaiDao.delete(theLoai1) ;
                                dialog.dismiss();
                            }
                        });
                        btn_No.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                bottomSheetView.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();

                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTheLoai.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      TextView txtMaLoai,txtTenLoai;
      CircleImageView imgHinh;
      RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaLoai=itemView.findViewById(R.id.txt_Maloai);
            txtTenLoai=itemView.findViewById(R.id.txt_TenLoai);
            imgHinh=itemView.findViewById(R.id.img_Hinh);
            relativeLayout=itemView.findViewById(R.id.realative_item);
            animationItem= AnimationUtils.loadAnimation(context,R.anim.anim_recyclerview);
            relativeLayout.setAnimation(animationItem);
        }
    }

}
