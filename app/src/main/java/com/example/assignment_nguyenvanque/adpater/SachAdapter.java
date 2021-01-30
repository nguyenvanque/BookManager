package com.example.assignment_nguyenvanque.adpater;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_nguyenvanque.DAO.SachDAO;
import com.example.assignment_nguyenvanque.DAO.TheLoaiDao;
import com.example.assignment_nguyenvanque.ListSachActivity;
import com.example.assignment_nguyenvanque.LoginActivity;
import com.example.assignment_nguyenvanque.R;
import com.example.assignment_nguyenvanque.model.Sach;
import com.example.assignment_nguyenvanque.model.TheLoai;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.ViewHolder> {
    Context context;
    ArrayList<Sach> listSach;
    ArrayList<TheLoai> listTheLoai = new ArrayList<>();
    SachDAO sachDAO;
    TheLoaiDao theLoaiDao;

    Dialog dialog;
    ArrayAdapter adapter;
    Spinner spMaTheLoai;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public SachAdapter(Context context, ArrayList<Sach> listTheLoai) {
        this.context = context;
        this.listSach = listTheLoai;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Sach sach = listSach.get(position);
        holder.txtTenSach.setText(sach.getTenSach());
        holder.txtSoluong.setText(sach.getSoLuong() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theLoaiDao = new TheLoaiDao(context);
                sachDAO = new SachDAO(context);
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_themsach);

                spMaTheLoai = dialog.findViewById(R.id.spnTheLoai);
                listTheLoai = theLoaiDao.getAllS();

//                Toast.makeText(context, sach.getMaSach()+"\n"+ sach.getTenSach()+"\n"+ sach.getMaTheLoai()+"\n"+sach.getNXB()+"\n"+sach.getTacGia()+"\n"+sach.getSoLuong()+"\n"+ sach.getGiaBia()+"", Toast.LENGTH_SHORT).show();
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        context, R.style.BottomSheetDialogTheme
                );

                final View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.bottom_sheet_dialog_sach,
                        (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
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
                                bottomSheetView.findViewById(R.id.txt_SuaSach).setVisibility(View.VISIBLE);
                                bottomSheetView.findViewById(R.id.txt_XoaSach).setVisibility(View.VISIBLE);
                                bottomSheetView.findViewById(R.id.viewSuaSach).setVisibility(View.VISIBLE);
                                bottomSheetView.findViewById(R.id.viewXoaSach).setVisibility(View.VISIBLE);                           }
                            if(accountType.equals("User")){
                                bottomSheetView.findViewById(R.id.txt_SuaSach).setVisibility(View.GONE);
                                bottomSheetView.findViewById(R.id.txt_XoaSach).setVisibility(View.GONE);
                                bottomSheetView.findViewById(R.id.viewSuaSach).setVisibility(View.GONE);
                                bottomSheetView.findViewById(R.id.viewXoaSach).setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                if(LoginActivity.setView==true){
//                    bottomSheetView.findViewById(R.id.txt_SuaSach).setVisibility(View.VISIBLE);
//                    bottomSheetView.findViewById(R.id.txt_XoaSach).setVisibility(View.VISIBLE);
//                    bottomSheetView.findViewById(R.id.viewSuaSach).setVisibility(View.VISIBLE);
//                    bottomSheetView.findViewById(R.id.viewXoaSach).setVisibility(View.VISIBLE);
//                }
//                if(LoginActivity.setView==false) {
//                    bottomSheetView.findViewById(R.id.txt_SuaSach).setVisibility(View.GONE);
//                    bottomSheetView.findViewById(R.id.txt_XoaSach).setVisibility(View.GONE);
//                    bottomSheetView.findViewById(R.id.viewSuaSach).setVisibility(View.GONE);
//                    bottomSheetView.findViewById(R.id.viewXoaSach).setVisibility(View.GONE);
//                }
                bottomSheetView.findViewById(R.id.txt_XemChiTietSach).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                context, R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(context).inflate(
                                R.layout.dialog_chi_tiet_sach,
                                (RelativeLayout) bottomSheetDialog.findViewById(R.id.realative_chitiet)
                        );

                        TextView txtMaTheLoai = bottomSheetView.findViewById(R.id.txt_MaTheLoai);
                        TextView txtTenSach = bottomSheetView.findViewById(R.id.txt_TenSach);
                        TextView txtMaSach = bottomSheetView.findViewById(R.id.txt_MaSach);
                        TextView txtNXB = bottomSheetView.findViewById(R.id.txt_NXB);
                        TextView txtGiaBia = bottomSheetView.findViewById(R.id.txt_GiaBia);
                        TextView txtTacGia = bottomSheetView.findViewById(R.id.txt_TacGia);
                        TextView txtSoLuong = bottomSheetView.findViewById(R.id.txt_SoLuong);
                        Button btnHuy = bottomSheetView.findViewById(R.id.btn_HuyChiTiet);
                        txtMaTheLoai.setText(sach.getMaTheLoai());
                        txtMaSach.setText(sach.getMaSach());
                        txtTenSach.setText(sach.getTenSach());
                        txtNXB.setText(sach.getNXB());
                        txtTacGia.setText(sach.getTacGia());
                        txtGiaBia.setText(sach.getGiaBia() + "");
                        txtSoLuong.setText(sach.getSoLuong() + "");


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
                bottomSheetView.findViewById(R.id.txt_SuaSach).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listTheLoai);
                        spMaTheLoai.setAdapter(adapter);

                        final EditText edt_MaSach = dialog.findViewById(R.id.edt_MaSach);
                        final EditText edt_TenSach = dialog.findViewById(R.id.edt_TenSach);
                        final EditText edt_TacGia = dialog.findViewById(R.id.edt_TacGia);
                        final EditText edt_NXB = dialog.findViewById(R.id.edtt_NXB);
                        final EditText edt_Soluong = dialog.findViewById(R.id.edt_SoLuong);
                        final EditText edt_GiaBia = dialog.findViewById(R.id.edt_GiaBia);
                        final Button btnThem = dialog.findViewById(R.id.btnAddBook);
                        final Button btnHuy = dialog.findViewById(R.id.btnShowBook);
                        btnThem.setText("Sửa");
                        btnHuy.setText("Hủy");
                        edt_MaSach.setText(sach.getMaSach());
                        edt_TenSach.setText(sach.getTenSach());
                        edt_TacGia.setText(sach.getTacGia());
                        edt_NXB.setText(sach.getNXB());
                        edt_GiaBia.setText(sach.getGiaBia() + "");
                        edt_Soluong.setText(sach.getSoLuong() + "");

                        int giatri = -1;
                        for (int i = 0; i < listTheLoai.size(); i++) {
                            if (listTheLoai.get(i).toString().equalsIgnoreCase(sach.getMaTheLoai())) {
                                giatri = i;
                                break;
                            }
                        }
                        spMaTheLoai.setSelection(giatri);
                        btnThem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String maSach = edt_MaSach.getText().toString();
                                String tenSach = edt_TenSach.getText().toString();
                                String NXB = edt_NXB.getText().toString();
                                String tacGia = edt_TacGia.getText().toString();
                                int soluong = Integer.parseInt(edt_Soluong.getText().toString());
                                double giaBia = Double.parseDouble(edt_GiaBia.getText().toString());
                                TheLoai ls = (TheLoai) spMaTheLoai.getSelectedItem();
                                String maTheLoai = ls.getMaTheLoai();
                                Sach sach = new Sach(maSach, maTheLoai, tenSach, tacGia, NXB, giaBia, soluong);
                                sachDAO.update(sach);
                                dialog.dismiss();
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
                bottomSheetView.findViewById(R.id.txt_XoaSach).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theLoaiDao=new TheLoaiDao(context);
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_xoa);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }

                        final TextView txt_Massage = dialog.findViewById(R.id.txt_Titleconfirm);
                        final Button btn_Yes = dialog.findViewById(R.id.btn_yes);
                        final Button btn_No = dialog.findViewById(R.id.btn_no);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);

                        progressBar.setVisibility(View.INVISIBLE);
                        final   Sach sach1=listSach.get(position);
                        txt_Massage.setText("Bạn có muốn xóa khóa học " + listSach.get(position).getTenSach() + " hay không ? ");
                        btn_Yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sachDAO.delete(sach1.getMaSach()) ;
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

                bottomSheetView.findViewById(R.id.txt_HuySach).setOnClickListener(new View.OnClickListener() {
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
        return listSach.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSach, txtSoluong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSoluong = itemView.findViewById(R.id.tvSoLuong);
            txtTenSach = itemView.findViewById(R.id.tvBookName);

        }
    }
}
