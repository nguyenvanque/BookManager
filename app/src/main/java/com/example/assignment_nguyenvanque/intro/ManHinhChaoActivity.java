package com.example.assignment_nguyenvanque.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.assignment_nguyenvanque.R;

public class ManHinhChaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chao);
        setTitle("BOOK MANAGER");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(ManHinhChaoActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
