package com.koonfungyee.midterm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(navListener);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        username=bundle.getString("username");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Bundle bundle=new Bundle();
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.newrestaurant:
                    intent=new Intent(MainActivity.this,newRestaurant.class);
                    bundle.putString("username",username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.promotion:
                    intent=new Intent(MainActivity.this,promotion.class);
                    bundle.putString("username",username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

                case R.id.profile:
                    intent=new Intent(MainActivity.this,profile.class);
                    bundle.putString("username",username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };
}
