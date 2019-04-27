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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class updateProfile extends AppCompatActivity {
    private String username;
    private int k;
    SQLiteDatabase db=null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView image;
    private EditText  name, phone, email;
    private Spinner gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        createdb();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        username=bundle.getString("username");
        setEmail(username);
        image=findViewById(R.id.profileImageUpdate);
        if (Drawable.createFromPath(imageloc(username))==null) {
            image.setImageResource(R.mipmap.user);
            k=1;
        }else {
            image.setImageDrawable(Drawable.createFromPath(imageloc(username)));
            k=2;
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Drawable.createFromPath(imageloc(username))==null){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(updateProfile.this);
                    alertDialogBuilder.setView(null);
                    alertDialogBuilder.setMessage("Change profile picture?")
                            .setPositiveButton("Change",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                            }
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    public String imageloc(String username){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("basic", Context.MODE_PRIVATE);
        File myimagepath = new File(directory, "/"+username+".jpg");
        return myimagepath.toString();
    }

    private void setEmail(String username) {
        name=findViewById(R.id.ETnameUpdate);
        phone=findViewById(R.id.ETphoneUpdate);
        gender=findViewById(R.id.SpinnerGender);
        email=findViewById(R.id.ETemailUpdate);
        String sqlsearch = "SELECT * FROM user WHERE username = '"+username+"'";
        Cursor c = db.rawQuery(sqlsearch,null);
        if (c.getCount()> 0){
            c.moveToFirst();
            String email1=c.getString(c.getColumnIndex("username"));
            String name1=c.getString(c.getColumnIndex("name"));
            String phone1=c.getString(c.getColumnIndex("phone"));
            String gender1=c.getString(c.getColumnIndex("gender"));
            email.setText(email1);
            email.setEnabled(false);
            name.setText(name1);
            phone.setText(phone1);
            int n=0;
            switch (gender1){
                case "":
                    n=0;
                    break;
                case "Male":
                    n=1;
                    break;
                case "Femle":
                    n=2;
            }
            gender.setSelection(n);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            image.buildDrawingCache();
        }
    }

    public void createdb(){
        db=this.openOrCreateDatabase("dbfood",MODE_PRIVATE,null);
        String sqlcreate="create table if not exists user"+
                "(username varchar not null, password varchar, name varchar, phone varchar, gender varchar, primary key(username));";
        db.execSQL(sqlcreate);
    }

    public void updateProfile(View view) {
        String name1=name.getText().toString();
        String phone1=phone.getText().toString();
        String gender1=gender.getSelectedItem().toString();
        String sqlupdate = "update user set name='"+name1+"', phone='"+phone1+"', gender='"+gender1+"' WHERE username = '"+username+"'";
        db.execSQL(sqlupdate);
        if (k==1){
            saveImage(username);
        }else if (k==2){
            deleteImage(username);
            saveImage(username);
        }

        Intent intent=new Intent(updateProfile.this,profile.class);
        Bundle bundle=new Bundle();
        bundle.putString("username",username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void deleteImage(String username){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("basic", Context.MODE_PRIVATE);
        File myimagepath = new File(directory, "/"+username+".jpg");
        if (myimagepath.exists()){
            myimagepath.delete();
        }
    }

    public void saveImage(String username) {
        // Create an image file name
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir("basic", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "/"+username+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Toast.makeText(cw,e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
