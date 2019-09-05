package com.restaurent.swaad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout homeLayout,searchLayout,cartLayout,accountLayout;
    private ImageView homeImage,searchIcon,cartIcon,accountIcon;
    private TextView homeTitle,searchTitle,cartTitle,accountTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeLayout = findViewById(R.id.home);
        homeImage = findViewById(R.id.home_icon);
        homeTitle = findViewById(R.id.home_title);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new HomeFragment());
        fragmentTransaction.commit();
        homeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.home));
        homeTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new HomeFragment());
                fragmentTransaction.commit();
              homeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.home));
                homeTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
              searchIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.search));
                searchTitle.setTextColor(Color.parseColor("#666666"));
                cartIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart));
                cartTitle.setTextColor(Color.parseColor("#666666"));
                accountIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.user));
                accountTitle.setTextColor(Color.parseColor("#666666"));
            }
        });
        searchLayout = findViewById(R.id.search);
        searchIcon = findViewById(R.id.search_icon);
        searchTitle = findViewById(R.id.search_title);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_unselected));
                homeTitle.setTextColor(Color.parseColor("#666666"));
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,new SearchFragment());
                fragmentTransaction.commit();
                searchIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_selected));
                searchTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                cartIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart));
                cartTitle.setTextColor(Color.parseColor("#666666"));
                accountIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.user));
                accountTitle.setTextColor(Color.parseColor("#666666"));
            }
        });
        cartLayout = findViewById(R.id.cart);
        cartIcon = findViewById(R.id.cart_icon);
        cartTitle = findViewById(R.id.cart_title);
        cartLayout.setOnClickListener(v->{
            cartIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_selected));
            cartTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            homeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_unselected));
            homeTitle.setTextColor(Color.parseColor("#666666"));
            searchIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.search));
            searchTitle.setTextColor(Color.parseColor("#666666"));
            accountIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.user));
            accountTitle.setTextColor(Color.parseColor("#666666"));
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.frame_layout,new CartFragment());
            fragmentTransaction1.commit();
        });
        accountLayout = findViewById(R.id.account);
        accountIcon = findViewById(R.id.account_icon);
        accountTitle = findViewById(R.id.account_title);
        accountLayout.setOnClickListener(v->{ accountIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_selected));
        accountTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        homeImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_unselected));
        homeTitle.setTextColor(Color.parseColor("#666666"));
            searchIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.search));
            searchTitle.setTextColor(Color.parseColor("#666666"));
           cartIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart));
           cartTitle.setTextColor(Color.parseColor("#666666"));
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.frame_layout,new AccountFragment());
            fragmentTransaction1.commit();
        });
    }
}
