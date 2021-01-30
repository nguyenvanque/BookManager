package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
EditText edtMatKhauMoi,edtMatKhauMoiXacNhan;
Button btnDoiPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edtMatKhauMoi=findViewById(R.id.edt_MatKhauMoi);
        edtMatKhauMoiXacNhan=findViewById(R.id.edt_MatKhauMoiXacNhan);
        btnDoiPassword=findViewById(R.id.btn_ChangePassword);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btnDoiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = edtMatKhauMoi.getText().toString();
                String pwdRe = edtMatKhauMoiXacNhan.getText().toString();
                if (pwd.isEmpty() && pwdRe.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Không được bỏ trống các trường", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals(pwdRe)) {
                    user.updatePassword(pwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu nhập lại không trùng vs mật khẩu đăng ký", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
