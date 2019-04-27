package com.koonfungyee.midterm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    CheckBox checkBox;
    EditText edusername1,edpassword1;
    Button btnlogin;
    SQLiteDatabase db=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edusername1 = findViewById(R.id.ETusername);
        edpassword1 = findViewById(R.id.ETpassword);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    String username = edusername1.getText().toString();
                    String pass = edpassword1.getText().toString();
                    savePref(username,pass);
                }
            }
        });
        loadPref();
        createdb();

        btnlogin = findViewById(R.id.BTNlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edusername1.getText().toString();
                String password = edpassword1.getText().toString();

                loginUser(username,password);
            }
        });
    }

    private void loadPref() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String premail = sharedPreferences.getString("email", "");
        String prpass = sharedPreferences.getString("password", "");
        if (premail.length()>0){
            checkBox.setChecked(true);
            edusername1.setText(premail);
            edpassword1.setText(prpass);
        }
    }

    private void savePref(String username, String pass) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", pass);
        editor.commit();
        Toast.makeText(this, "Preferences has been saved", Toast.LENGTH_SHORT).show();
    }

    public void createdb(){
        db=this.openOrCreateDatabase("dbfood",MODE_PRIVATE,null);
        String sqlcreate="create table if not exists user"+
                "(username varchar not null, password varchar, name varchar, phone varchar, gender varchar, primary key(username));";
        db.execSQL(sqlcreate);
    }

    private void loginUser(String username, String pass) {
        String sqlsearch="select * from user where username like '"+username+"' and password like '"+pass+"'";
        Cursor c = db.rawQuery(sqlsearch,null);
        if (c.getCount()>0){
            Bundle bundle=new Bundle();
            bundle.putString("username",username);
            Intent intent=new Intent(this,MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Incorrect password or username", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickhere(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }
}
