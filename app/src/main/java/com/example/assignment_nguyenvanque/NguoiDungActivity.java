package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.assignment_nguyenvanque.fragment.ProfileFragment;
import com.example.assignment_nguyenvanque.fragment.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class NguoiDungActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_dung);
        setTitle("NGƯỜI DÙNG");

        navigationView=findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        ProfileFragment fragment1=new ProfileFragment();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.containner,fragment1,"");
        ft1.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_profile:
                    ProfileFragment fragment2=new ProfileFragment();
                    FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.containner,fragment2,"");
                    ft2.commit();
                    return true;
                case R.id.nav_users:
                    UsersFragment fragment3=new UsersFragment();
                    FragmentTransaction ft13=getSupportFragmentManager().beginTransaction();
                    ft13.replace(R.id.containner,fragment3,"");
                    ft13.commit();
                    return true;
            }
            return false;
        }
    };
}
