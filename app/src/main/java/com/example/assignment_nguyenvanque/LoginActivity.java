package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText edtemail, edtpassword;
    Button btnLogin;
    TextView txtRegister, txt_recover;
    CheckBox checkBox;

    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;

    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Đang xử lý...");
        edtemail = findViewById(R.id.edt_email);
        edtpassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);
        txt_recover = findViewById(R.id.txt_recover);
        checkBox = findViewById(R.id.chkRememberPass);
        setTitle("LOGIN");
        layThongTin();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Không đúng định dạng email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    loginAccount(email, password);
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        txt_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.dialog_recoverpassword);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edtEmail = dialog.findViewById(R.id.edt_email);
                Button btnSendEmail = dialog.findViewById(R.id.btn_SendEmail);
                Button btnHuy = dialog.findViewById(R.id.btn_Huy);
                btnSendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = edtEmail.getText().toString();
                        if (edtEmail.getText().toString().isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                        } else {
                            beginRecovery(email);
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
    }

    private void beginRecovery(String email) {
        progressDialog.setMessage("Send email...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra gmail của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loginAccount(final String email, final String password) {
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            luuThongTin();
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void layThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("Book", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("checkstatus", false);
        if (check) {
            String tenNguoiDung = sharedPreferences.getString("tennguoidung", "");
            String matKhau = sharedPreferences.getString("matkhau", "");
            edtemail.setText(tenNguoiDung);
            edtpassword.setText(matKhau);
        } else {
            edtemail.setText("");
            edtpassword.setText("");
        }
        checkBox.setChecked(check);
    }

    private void luuThongTin() {
        SharedPreferences sharedPreferences = getSharedPreferences("Book", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String ten = edtemail.getText().toString();
        String pass = edtpassword.getText().toString();
        boolean check = checkBox.isChecked();
        if (!check) {
            editor.clear();
        } else {
            editor.putString("tennguoidung", ten);
            editor.putString("matkhau", pass);
            editor.putBoolean("checkstatus", check);
        }
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
