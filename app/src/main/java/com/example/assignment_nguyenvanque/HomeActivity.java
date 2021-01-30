package com.example.assignment_nguyenvanque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    ImageView imgSetting,user_photo;
    LinearLayout gridLayout;
    View viewProfile;
    Animation animationGird,animationCard2,animationCard3;
    CardView cardViewNguoiDung,cardViewTheLoai,cardViewSach,cardViewHoaDon,cardViewThongKe,cardViewSachBanChay;

    ImageView imgMisFotos, imgAnim, imgAnim2;
    Handler handlerAnimationCIMG;
    TextView txtEmail,txtName;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    LinearLayout linearLayout;
    public static boolean isDark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("HOME");

        imgSetting=findViewById(R.id.img_setting);
        linearLayout=findViewById(R.id.linelayoutHome);
        gridLayout=findViewById(R.id.linelayout);
        txtEmail=findViewById(R.id.txt_Email);
        txtName=findViewById(R.id.txt_Name);
        cardViewNguoiDung=findViewById(R.id.cardview_nguoidung);
        cardViewSach=findViewById(R.id.cardview_sach);
        cardViewTheLoai=findViewById(R.id.cardview_theloai);
        cardViewHoaDon=findViewById(R.id.cardview_hoadon);
        cardViewThongKe=findViewById(R.id.cardview_ThongKe);
        cardViewSachBanChay=findViewById(R.id.cardview_Sachbanchay);



        reference= FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        checkAccountTytype();
//        user_photo=findViewById(R.id.user_photo);

        isDark = getThemeStatePref();
        if(isDark) {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.black));
        } else
        {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }


        this.handlerAnimationCIMG = new Handler();
        this.viewProfile = findViewById(R.id.viewProfile);
        this.imgMisFotos = findViewById(R.id.imgMiphoto);
        this.imgAnim = findViewById(R.id.imgAnim);
        this.imgAnim2 = findViewById(R.id.imgAnim2);


        txtEmail.setText(firebaseUser.getEmail());

        runnableAnim.run();

        animationGird= AnimationUtils.loadAnimation(HomeActivity.this,R.anim.bottom_to_top1);
        animationCard2= AnimationUtils.loadAnimation(HomeActivity.this,R.anim.bottom_to_top2);
        animationCard3= AnimationUtils.loadAnimation(HomeActivity.this,R.anim.bottom_to_top3);

        cardViewNguoiDung.setAnimation(animationGird);
        cardViewTheLoai.setAnimation(animationGird);

        cardViewSach.setAnimation(animationCard2);
        cardViewHoaDon.setAnimation(animationCard2);

        cardViewSachBanChay.setAnimation(animationCard3);
        cardViewThongKe.setAnimation(animationCard3);




        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view=(ImageView)v;
                final PopupMenu popupMenu=new PopupMenu(HomeActivity.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_setting,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.menu_changepassword:
                              startActivity(new Intent(HomeActivity.this,ChangePasswordActivity.class));
                                break;
                            case R.id.menu_logout:
                                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance() ;
                                firebaseAuth.signOut();
                                Intent intent2=new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent2);
                                finish();
                                break;
                            case R.id.menu_introduction:
                                isDark = !isDark ;
                                if (isDark) {
                                    linearLayout.setBackgroundColor(getResources().getColor(R.color.black));
                                }
                                else {
                                    linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                                }
                                saveThemeStatePref(isDark);
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        cardViewNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomeActivity.this, NguoiDungActivity.class);
                startActivity(intent2);
            }
        });
        cardViewSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomeActivity.this, ListSachActivity.class);
                startActivity(intent2);
             //  finish();
            }
        });
        cardViewHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomeActivity.this, ListHoaDonActivity.class);
                startActivity(intent2);
            }
        });
        cardViewTheLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomeActivity.this, ListTheLoaiActivity.class);
                startActivity(intent2);
                // finish();
            }
        });
        cardViewSachBanChay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomeActivity.this, LuotSachBanChayActivity.class);
                startActivity(intent2);
            }
        });
        cardViewThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(HomeActivity.this, ThongKeActivity.class);
                startActivity(intent2);
            }
        });
    }
    private void saveThemeStatePref(boolean isDark) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref2",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDark",isDark);
        editor.commit();
    }
    private boolean getThemeStatePref () {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref2",MODE_PRIVATE);
        boolean isDark = pref.getBoolean("isDark",false) ;
        return isDark;
    }
    private Runnable runnableAnim = new Runnable() {
        @Override
        public void run() {
            imgAnim.animate().scaleX(1.7f).scaleY(1.7f).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imgAnim.setScaleX(1f);
                    imgAnim.setScaleY(1f);
                    imgAnim.setAlpha(1f);
                }
            });
            imgAnim2.animate().scaleX(1.7f).scaleY(1.7f).alpha(0f).setDuration(700).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imgAnim2.setScaleX(1f);
                    imgAnim2.setScaleY(1f);
                    imgAnim2.setAlpha(1f);
                }
            });
            handlerAnimationCIMG.postDelayed(runnableAnim, 1500);
        }
    };
    public void checkAccountTytype(){
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
                    String image = "" + ds.child("image").getValue();
                    String name = "" + ds.child("name").getValue();
                    txtName.setText(name);
                    if(accountType.equals("Admin")){
                        cardViewSachBanChay.setVisibility(View.VISIBLE);
                        cardViewThongKe.setVisibility(View.VISIBLE);
                    }
                    if(accountType.equals("User")){
                        cardViewSachBanChay.setVisibility(View.GONE);
                        cardViewThongKe.setVisibility(View.GONE);
                    }
                    try {
                        if(!image.equals("")){
                            Picasso.with(HomeActivity.this).load(image).placeholder(R.drawable.ic_photo_user).into(imgMisFotos);
                        }   else {
                            imgMisFotos.setImageResource(R.drawable.ic_photo_user);
                        }
                    }catch (Exception e){
                        imgMisFotos.setImageResource(R.drawable.ic_photo_user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void checkUserStatus() {
        if (firebaseUser != null) {

        } else {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }
    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
