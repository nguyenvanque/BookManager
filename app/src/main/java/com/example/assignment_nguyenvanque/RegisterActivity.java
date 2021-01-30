package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName,edtemail, edtpassword,edtConfirm;
    Button btnRegister;
    TextView txtLogin;
    Spinner spinnerAccount;

   FirebaseAuth auth;
   ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtemail = findViewById(R.id.edt_email);
        edtName = findViewById(R.id.edt_name);
        edtpassword = findViewById(R.id.edt_password);
        edtConfirm = findViewById(R.id.edt_ConfirmPassword);
        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_login);
        spinnerAccount = findViewById(R.id.spiner_Account);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type, R.layout.support_simple_spinner_dropdown_item);
        spinnerAccount.setAdapter(adapter);
        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Đang xử lý...");

        auth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();
                String passwordCf = edtConfirm.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Tên không được bỏ trống", Toast.LENGTH_SHORT).show();
                }
               else if(email.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Email không được bỏ trống", Toast.LENGTH_SHORT).show();
                }else  if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
                }else if(passwordCf.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không được bỏ trống", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(passwordCf)){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không đúng", Toast.LENGTH_SHORT).show();
                }
                else {
                    createAccount(name,email, password);
                }

            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

    }

    private void createAccount(final String name, String email, String password) {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();

                            String email=user.getEmail();
                            String uid=user.getUid();

                            HashMap<Object,String> hashMap=new HashMap<>();
                            // put info in hasmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",""+name);  //sẽ add trong sửa thông tin
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("cover","");
                            hashMap.put("accountType",spinnerAccount.getSelectedItem().toString());
                            // firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            //put data within hasmap in database
                            reference.child(uid).setValue(hashMap);

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                            progressDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });

    }
}
