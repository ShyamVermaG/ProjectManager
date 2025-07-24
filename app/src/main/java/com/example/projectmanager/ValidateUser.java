package com.example.projectmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ValidateUser extends AppCompatActivity {

    TextInputLayout pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_user);

        pass=findViewById(R.id.password);
    }

    public void checkLogin(View view) {
        if(pass.getEditText().getText().toString().equals("tinku")){
            startActivity(new Intent(this,MainActivity.class));

            finish();
        }else{
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
        }

    }
}