package com.example.newproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etUsername,etPassword;
    Button btnLogin;
    androidx.appcompat.app.AlertDialog.Builder builder;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeId();
        builder = new AlertDialog.Builder( this );
    }
    private void initializeId() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etpassword);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        {
            switch (view.getId()) {
                case R.id.btn_login:
                    validationextras();
                    break;
            }
        }

    }

    private void checkValidations() {
        Intent intent = new Intent( LoginActivity.this, MainActivity.class );
        startActivity( intent );
    }
    private void validationextras(){
        String passwordInput = etPassword.getText().toString().trim();
        if (etUsername.getText().toString().trim().length() > 10 || etUsername.getText().toString().trim().length() < 10) {
            builder.setMessage( "Invalid Number" ).setTitle( "Invalid Number" );
            //Setting message manually and performing action on button click
            builder.setMessage( "Please Enter Valid Phone Number" )
                    .setCancelable( false )
                    .setPositiveButton( "ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            etUsername.setText(" ");
                            dialog.cancel();
                        }
                    } );
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.setIcon( R.drawable.ic_error );
            alert.setTitle( "Invalid Number" );
            alert.show();
            //TO set height and width of alert dialog
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            //lp.width = 900;
            lp.height = 500;
            alert.getWindow().setAttributes(lp);
            //setting validations for password
        }else if (etPassword.getText().length() == 0 ){
            builder.setMessage( "Enter Password" ).setTitle( "Enter Password" );
            builder.setMessage( "Please enter password" )
                    .setCancelable( false )
                    .setPositiveButton( "ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    } );
            AlertDialog alert = builder.create();
            alert.setIcon( R.drawable.ic_error );
            alert.setTitle( "Please enter number" );
            alert.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            //lp.width = 900;
            lp.height = 500;
            alert.getWindow().setAttributes(lp);
        }else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            builder.setMessage( "Low Password" ).setTitle( "Password is too low" );
            builder.setMessage( "Please enter strong password" )
                    .setCancelable( false )
                    .setPositiveButton( "ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    } );
            AlertDialog alert = builder.create();
            alert.setIcon( R.drawable.ic_error );
            alert.setTitle( "Invalid Number" );
            alert.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert.getWindow().getAttributes());
            //lp.width = 900;
            lp.height = 500;
            alert.getWindow().setAttributes(lp);

        }else {
            Intent intent = new Intent( LoginActivity.this, MainActivity.class );
            startActivity( intent );
        }
    }
}