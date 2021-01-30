package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuaTheLoaiActivity extends AppCompatActivity {
    ImageView imgChonHinh;
    EditText edtMaTheLoai, edtTenTheLoai, edtMoTa, edtViTri;
    Button btnSua, btnHuy;
    ProgressDialog progressDialog;

    public static ArrayList<TheLoai> list;
    TheLoaiDao theLoaiDao;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageReference;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermission;
    private String[] storagePermission;

    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_the_loai);
        setTitle("SỬA THỂ LOẠI");
        progressDialog=new ProgressDialog(SuaTheLoaiActivity.this);
        progressDialog.setMessage("Đang xử lí...");

        theLoaiDao = new TheLoaiDao(SuaTheLoaiActivity.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("TheLoai");
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://assignmentnguyenvanque.appspot.com");

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        edtMaTheLoai = findViewById(R.id.edt_MaTheLoai);
        edtTenTheLoai = findViewById(R.id.edt_TenTheLoai);
        edtMoTa = findViewById(R.id.edt_MoTa);
        edtViTri = findViewById(R.id.edt_ViTri);
        imgChonHinh = findViewById(R.id.img_ChonHinh);
        btnSua = findViewById(R.id.btn_Them);
        btnHuy = findViewById(R.id.btn_Huy);
        edtMaTheLoai.setEnabled(false);

        final TheLoai theLoai = (TheLoai) getIntent().getSerializableExtra("mybook");
        edtMaTheLoai.setText(theLoai.getMaTheLoai());
        edtTenTheLoai.setText(theLoai.getTenTheLoai());
        edtMoTa.setText(theLoai.getMoTa());
        edtViTri.setText(theLoai.getViTri() + "");
        if(theLoai.getHinhAnh()==""){
            imgChonHinh.setImageResource(R.drawable.ic_face);
        }else {
            Picasso.with(SuaTheLoaiActivity.this).load(theLoai.getHinhAnh()).into(imgChonHinh);

        }
        imgChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showImagePickDialog();

                }catch (Exception e){
                }
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (image_uri == null) {
                    String ma   = edtMaTheLoai.getText().toString();
                    String ten  = edtTenTheLoai.getText().toString();
                    String moTa = edtMoTa.getText().toString();
                    int vitri   = Integer.parseInt(edtViTri.getText().toString());
                    String hinh = theLoai.getHinhAnh();
                    TheLoai theLoai = new TheLoai(ma, ten, moTa, vitri, hinh);
                    theLoaiDao.update(theLoai);
                    progressDialog.dismiss();
                } else {
                    final StorageReference mref = storageReference.child("imageTheLoai").child(myRef.push().getKey());
                    mref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            String hinhUri  = String.valueOf(downloadUri);
                            String ma       = edtMaTheLoai.getText().toString();
                            String ten      = edtTenTheLoai.getText().toString();
                            String moTa     = edtMoTa.getText().toString();
                            int vitri = Integer.parseInt(edtViTri.getText().toString());
                            TheLoai theLoai = new TheLoai(ma, ten, moTa, vitri, hinhUri);
                            theLoaiDao.update(theLoai);
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SuaTheLoaiActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuaTheLoaiActivity.this, ListTheLoaiActivity.class));
            }
        });
    }
    public void showImagePickDialog() {
        String option[] = {"Camera", "Thư viện ảnh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SuaTheLoaiActivity.this);
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
        boolean result = ContextCompat.checkSelfPermission(SuaTheLoaiActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragetPermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(SuaTheLoaiActivity.this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(SuaTheLoaiActivity.this,
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
        image_uri = SuaTheLoaiActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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
                        Toast.makeText(SuaTheLoaiActivity.this, "Khong try cap duoc camera", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccpted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccpted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(SuaTheLoaiActivity.this, "Vui lòng bật quyền thư viện", Toast.LENGTH_SHORT).show();
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
}
