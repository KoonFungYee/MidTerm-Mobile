package com.koonfungyee.midterm;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class profile extends AppCompatActivity {
    private BottomNavigationView navigation;
    SQLiteDatabase db=null;
    private TextView tvname,tvemail,tvphone,tvgender;
    private String username;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(navListener);
        createdb();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        username=bundle.getString("username");
        tvname=findViewById(R.id.TVname);
        tvemail=findViewById(R.id.TVemail);
        tvphone=findViewById(R.id.TVphone);
        tvgender=findViewById(R.id.TVgender);
        fillAll(username);
        image=findViewById(R.id.imageprofile);
        if (Drawable.createFromPath(imageloc(username))==null) {
            image.setImageResource(R.mipmap.user);
        }else {
            image.setImageDrawable(Drawable.createFromPath(imageloc(username)));
        }
    }

    private String imageloc(String username) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("basic", Context.MODE_PRIVATE);
        File myimagepath = new File(directory, "/"+username+".jpg");
        return myimagepath.toString();
    }

    private void fillAll(String username) {
        String sqlsearch="select * from user where username like '"+username+"'";
        Cursor c = db.rawQuery(sqlsearch,null);
        if (c.getCount()>0){
            c.moveToFirst();
            String name=c.getString(c.getColumnIndex("name"));
            String phone=c.getString(c.getColumnIndex("phone"));
            String gender=c.getString(c.getColumnIndex("gender"));
            tvname.setText(name);
            tvemail.setText(username);
            tvphone.setText(phone);
            tvgender.setText(gender);
        }
    }

    public void createdb(){
        db=this.openOrCreateDatabase("dbfood",MODE_PRIVATE,null);
        String sqlcreate="create table if not exists user"+
                "(username varchar not null, password varchar, name varchar, phone varchar, gender varchar, primary key(username));";
        db.execSQL(sqlcreate);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Bundle bundle=new Bundle();
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.newrestaurant:
                    intent=new Intent(profile.this,newRestaurant.class);
                    bundle.putString("username",username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.promotion:
                    intent=new Intent(profile.this,promotion.class);
                    bundle.putString("username",username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    intent=new Intent(profile.this,profile.class);
                    bundle.putString("username",username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };

    public void logout(View view) {
        startActivity(new Intent(this,Login.class));
        finish();
    }

    public void edit(View view) {
        Intent intent=new Intent(profile.this,updateProfile.class);
        Bundle bundle=new Bundle();
        bundle.putString("username",username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
