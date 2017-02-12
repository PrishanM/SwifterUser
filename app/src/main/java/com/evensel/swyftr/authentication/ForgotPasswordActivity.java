package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ValidatorUtil;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class ForgotPasswordActivity extends Activity implements View.OnClickListener {

    private TextView txtBackToLogin;
    private Button btnReset;
    private EditText txtEmailAddress;
    private LayoutInflater inflate;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Initialize UI components
        txtBackToLogin = (TextView)findViewById(R.id.txtBackToLogin);
        txtEmailAddress = (EditText)findViewById(R.id.txtEmailAddress);
        btnReset = (Button)findViewById(R.id.btnReset);

        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

        //Assign click listeners
        txtBackToLogin.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtBackToLogin){
            finish();
        }else if(view.getId()==R.id.btnReset){
            if(txtEmailAddress.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Username cannot be empty.").show();
            }else if(!ValidatorUtil.isValidEmailAddress(txtEmailAddress.getText().toString())){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Invalid email address.").show();
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                // Setting Dialog Title
                alertDialog.setTitle("SENT");
                // Setting Dialog Message
                alertDialog.setMessage("Password reset code has been sent successfully");
                // Setting OK Button
                alertDialog.setButton("Back to Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        }
    }
}
