package com.koonfungyee.midterm;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText username, password, name, phone;
    Spinner sex;
    SQLiteDatabase db=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createdb();
    }

    public void createdb(){
        db=this.openOrCreateDatabase("dbfood",MODE_PRIVATE,null);
        String sqlcreate="create table if not exists user"+
                "(username varchar not null, password varchar, name varchar, phone varchar, gender varchar, primary key(username));";
        db.execSQL(sqlcreate);
    }

    public void clickhere(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }

    public void register(View view) {
        username=findViewById(R.id.ETusername);
        password=findViewById(R.id.ETpassword);
        name=findViewById(R.id.ETname);
        phone=findViewById(R.id.ETphone);
        sex=findViewById(R.id.spinner);

        String username1=username.getText().toString();
        String password1=password.getText().toString();
        String name1=name.getText().toString();
        String phone1=phone.getText().toString();
        String gender=sex.getSelectedItem().toString();

        String sqlsearch="select * from user where username like '"+username1+"'";
        Cursor c = db.rawQuery(sqlsearch,null);

        if (username1.equals("")||password1.equals("")||name1.equals("")||phone1.equals("")||gender.equals("")){
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }else if (c.getCount()>0){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder.setView(null);
            alertDialogBuilder.setCancelable(true)
                    .setMessage("This username/email has been sign up, kindly click Yes to login")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    startActivity(new Intent(Register.this,Login.class));
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

        }else {
            registerUser(username1,password1,name1,phone1,gender);
        }

    }

    private void registerUser(String username, String password, String name, String phone, String gender) {
        String sqlinsert="insert into user (username,password,name,phone,gender) " +
                "values ('"+username+"','"+password+"','"+name+"','"+phone+"','"+gender+"')";
        db.execSQL(sqlinsert);
        Toast.makeText(this, "Successfully sign up", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Register.this, Login.class));
    }
}
