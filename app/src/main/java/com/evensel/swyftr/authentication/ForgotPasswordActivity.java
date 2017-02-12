package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.evensel.swyftr.R;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class ForgotPasswordActivity extends Activity implements View.OnClickListener {

    private TextView txtBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Initialize UI components
        txtBackToLogin = (TextView)findViewById(R.id.txtBackToLogin);

        //Assign click listeners
        txtBackToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtBackToLogin){
            finish();
        }
    }
}
