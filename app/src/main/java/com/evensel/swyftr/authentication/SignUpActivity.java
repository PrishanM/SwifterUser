package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ResponseModel;
import com.evensel.swyftr.util.ValidatorUtil;

/**
 * Created by Prishan Maduka on 2/11/2017.
 */
public class SignUpActivity extends Activity implements View.OnClickListener {

    private TextView txtBackLogin;
    private EditText txtEmail,txtPassword,txtRepeatPassword,txtMobileNumber;
    private Button btnSignUp;
    private LayoutInflater inflate;
    private View layout;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing UI components
        txtBackLogin = (TextView)findViewById(R.id.txtBackToLogin);
        txtEmail = (EditText)findViewById(R.id.txtEmailAddress);
        txtMobileNumber = (EditText)findViewById(R.id.txtMobileNumber);
        txtRepeatPassword = (EditText)findViewById(R.id.txtRepeatPassword);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

        //Assigning click listeners
        txtBackLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtBackToLogin){
            finish();
        }else if(view.getId()==R.id.btnSignUp){
            if(txtEmail.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Email address cannot be empty.").show();
            }else if(!ValidatorUtil.isValidEmailAddress(txtEmail.getText().toString())){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Invalid email address.").show();
            }else if(txtMobileNumber.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Mobile number cannot be empty.").show();
            }else if(txtPassword.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Password cannot be empty.").show();
            }else{
                String message = ValidatorUtil.isValidPassword(txtPassword.getText().toString());
                if(!message.equalsIgnoreCase("Success")){
                    Notifications.showToastMessage(layout,getApplicationContext(),message).show();
                }else{
                    if(txtPassword.getText().toString().equalsIgnoreCase(txtRepeatPassword.getText().toString())){
                        progress = ProgressDialog.show(SignUpActivity.this, null,
                                "Creating user...", true);
                        JsonRequestManager.getInstance(SignUpActivity.this).registerUserRequest(AppURL.APPLICATION_BASE_URL+AppURL.USER_REGISTER_URL, txtEmail.getText().toString(),txtPassword.getText().toString(),txtRepeatPassword.getText().toString(),txtMobileNumber.getText().toString(), requestCallback);
                    }else{
                        Notifications.showToastMessage(layout,getApplicationContext(),"Repeat password does not match.").show();
                    }

                }
            }
        }
    }

    //Response callback for "User Registration"
    private final JsonRequestManager.registerUser requestCallback = new JsonRequestManager.registerUser() {

        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
                // Setting Dialog Message
                alertDialog.setMessage("User registered successfully");
                // Setting OK Button
                alertDialog.setButton("Back to Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }else{
                String message = "";
                for (int i=0;i<model.getDetails().size();i++){
                    message = message + "\n";
                }
                Notifications.showGeneralDialog(SignUpActivity.this,message).show();
            }


        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showGeneralDialog(SignUpActivity.this,status).show();
        }
    };
}
