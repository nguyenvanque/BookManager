package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment_nguyenvanque.DAO.TheLoaiDao;
import com.example.assignment_nguyenvanque.adpater.TheLoaiAdapter;
import com.example.assignment_nguyenvanque.model.TheLoai;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class ListTheLoaiActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ImageView imgChonHinh;
    SearchView searchView;
    public static ArrayList<TheLoai> list;
    public static TheLoaiAdapter theLoaiAdapter;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, fbThemLoaiSach;
    TheLoaiDao theLoaiDao;
    FirebaseDatabase database;
    DatabaseReference myRef;

    StorageReference storageReference;
    StorageReference mref;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;


    private String[] cameraPermission;
    private String[] storagePermission;

    Uri image_uri = null;

    public static boolean isDark = false;
    RelativeLayout relativeLayout;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_the_loai);

        setTitle("THỂ LOẠI");
        progressDialog = new ProgressDialog(ListTheLoaiActivity.this);
        progressDialog.setMessage("Đang xử lý...");
        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = findViewById(R.id.material_design_floating_action_menu_item2);
        fbThemLoaiSach = findViewById(R.id.fb_themLoaiSach);
        recyclerView = findViewById(R.id.recyclerview_TheLoai);
        searchView = findViewById(R.id.search);
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        searchView.setActivated(true);
        searchView.setQueryHint(Html.fromHtml("<font color = #5E5E5E>" + "Nhập thể loại cần tìm" + "</font>"));
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        relativeLayout = findViewById(R.id.realative_TheLoai);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("TheLoai");
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://assignmentnguyenvanque.appspot.com");
        checkAccountType();

        if (HomeActivity.isDark == true) {
            // dark theme is on
            autoComplete.setTextColor(Color.WHITE);
            searchView.setQueryHint(Html.fromHtml("<font color = #fff>" + "Nhập thể loại cần tìm" + "</font>"));
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            // light theme is on
            autoComplete.setTextColor(Color.BLACK);
            searchView.setQueryHint(Html.fromHtml("<font color = #5E5E5E>" + "Nhập thể loại cần tìm" + "</font>"));
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }


        theLoaiDao = new TheLoaiDao(ListTheLoaiActivity.this);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListTheLoaiActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        loadData();



        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new
//                        Intent(ListTheLoaiActivity.this, ChangePasswordActivity.class);
//                startActivity(intent);
            }
        });
        fbThemLoaiSach.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ListTheLoaiActivity.this);
                dialog.setContentView(R.layout.dialog_themloaisach);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edt_MaTheLoai = dialog.findViewById(R.id.edt_MaTheLoai);
                final EditText edt_TenTheLoai = dialog.findViewById(R.id.edt_TenTheLoai);
                final EditText edt_MoTa = dialog.findViewById(R.id.edt_MoTa);
                final EditText edt_ViTri = dialog.findViewById(R.id.edt_ViTri);
                imgChonHinh = dialog.findViewById(R.id.img_ChonHinh);
                final Button btnThem = dialog.findViewById(R.id.btn_Them);
                final Button btnHuy = dialog.findViewById(R.id.btn_Huy);
                imgChonHinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            showImagePickDialog();

                        }catch (Exception e){
                        }
                    }
                });
                btnThem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        final String ma = edt_MaTheLoai.getText().toString();
                        final String ten = edt_TenTheLoai.getText().toString();
                        final String mota = edt_MoTa.getText().toString();
                        final String vitri = edt_ViTri.getText().toString();



                        if (ma.isEmpty()) {
                            Toast.makeText(ListTheLoaiActivity.this, "Mã thể loại không được bỏ trống", Toast.LENGTH_SHORT).show();
                            edt_MaTheLoai.setFocusable(true);
                            progressDialog.dismiss();
                            return;
                        } else if (ten.isEmpty()) {
                            Toast.makeText(ListTheLoaiActivity.this, "Tên thể loại không được bỏ trống", Toast.LENGTH_SHORT).show();
                            edt_TenTheLoai.setFocusable(true);
                            progressDialog.dismiss();
                            return;
                        } else if (mota.isEmpty()) {
                            Toast.makeText(ListTheLoaiActivity.this, "Mô tả  không được bỏ trống", Toast.LENGTH_SHORT).show();
                            edt_MoTa.setFocusable(true);
                            progressDialog.dismiss();
                            return;
                        } else if (TextUtils.isEmpty(edt_ViTri.getText().toString())) {
                            Toast.makeText(ListTheLoaiActivity.this, "Vị trí  không được bỏ trống", Toast.LENGTH_SHORT).show();
                            edt_ViTri.setFocusable(true);
                            progressDialog.dismiss();
                            return;
                        }
                        else if(ten.matches((".*[0-9].*"))){
                            Toast.makeText(ListTheLoaiActivity.this, "Tên thể loại phải là chữ", Toast.LENGTH_SHORT).show();
                             progressDialog.dismiss();
                            return;
                        }
                        else if(mota.matches((".*[0-9].*"))){
                            Toast.makeText(ListTheLoaiActivity.this, "Mô tả thể loại phải là chữ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        else if(!vitri.matches((".*[0-9].*"))){
                            Toast.makeText(ListTheLoaiActivity.this, "Vị trí thể loại phải là số", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        if (xetTrung(ma)) {
                            Toast.makeText(ListTheLoaiActivity.this, "Mã thể loại không được trùng", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        } else {
                            if (image_uri == null) {
                                ProgressDialog progressDialog1=new ProgressDialog(ListTheLoaiActivity.this);
                                progressDialog1.setMessage("Đang xử lí...");
                                progressDialog1.show();
                                if (vitri.isEmpty()) {
                                    Toast.makeText(ListTheLoaiActivity.this, "Vị trí  không được bỏ trống", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                    progressDialog1.dismiss();

                                } else {
                                    TheLoai theLoai = new TheLoai(ma, ten, mota, Integer.parseInt(vitri), "");
                                    theLoaiDao.insertS(theLoai);

                                    progressDialog.dismiss();
                                    progressDialog1.dismiss();
                                    dialog.dismiss();
                                }


                            } else {
                                progressDialog.show();

                                mref = storageReference.child("imageTheLoai").child(myRef.push().getKey());
                                mref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful()) ;
                                        Uri downloadUri = uriTask.getResult();
                                        String hinhUri = String.valueOf(downloadUri);
                                        list.clear();
                                        TheLoai theLoai = new TheLoai(ma, ten, mota, Integer.parseInt(vitri), hinhUri);
                                        theLoaiDao.insertS(theLoai);
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ListTheLoaiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchPost(query);
                } else {
                    loadData();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPost(newText);

                return false;
            }
        });
    }

    public boolean xetTrung(String maTheLoai) {
        Boolean xet = false;
        for (int i = 0; i < list.size(); i++) {
            String ma = list.get(i).getMaTheLoai();
            if (ma.equalsIgnoreCase(maTheLoai)) {
                xet = true;
                break;
            }
        }
        return xet;
    }

    private void searchPost(final String query) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TheLoai modelPost = ds.getValue(TheLoai.class);
                    if (modelPost.getTenTheLoai().toLowerCase().contains(query.toLowerCase()) ||
                            modelPost.getMaTheLoai().toLowerCase().contains(query.toLowerCase())) {
                        list.add(modelPost);
                    }

                    theLoaiAdapter = new TheLoaiAdapter(ListTheLoaiActivity.this, list);
                    recyclerView.setAdapter(theLoaiAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(ListTheLoaiActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        list = theLoaiDao.getAll();
        theLoaiAdapter = new TheLoaiAdapter(ListTheLoaiActivity.this, list);
        recyclerView.setAdapter(theLoaiAdapter);
    }

    public void showImagePickDialog() {
        String option[] = {"Camera", "Thư viện ảnh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ListTheLoaiActivity.this);
        builder.setTitle("Mời bạn chọn");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragetPermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(ListTheLoaiActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragetPermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(ListTheLoaiActivity.this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(ListTheLoaiActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        image_uri = ListTheLoaiActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccept && storageAccept) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(ListTheLoaiActivity.this, "Khong try cap duoc camera", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccpted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccpted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(ListTheLoaiActivity.this, "Vui lòng bật quyền thư viện", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                Picasso.with(this).load(image_uri).into(imgChonHinh);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                Picasso.with(this).load(image_uri).into(imgChonHinh);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveThemeStatePref(boolean isDark) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDark", isDark);
        editor.commit();
    }

    private boolean getThemeStatePref() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = pref.getBoolean("isDark", false);
        return isDark;

    }
    public void checkAccountType(){
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
                        materialDesignFAM.setVisibility(View.VISIBLE);
                    }
                    if(accountType.equals("User")){
                        materialDesignFAM.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
