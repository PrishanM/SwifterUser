package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.evensel.swyftr.R;

/**
 * Created by Prishan Maduka on 2/11/2017.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView txtForgotPassword,txtSignUp;
    private EditText txtUserName,txtPassword;
    private Button btnLogin;
    private ImageView btnFacebook,btnTwitter,btnGooglePlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing UI components
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);

        //Assigning click listeners
        txtForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtForgotPassword){
            Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(intent);
        }
    }
}
