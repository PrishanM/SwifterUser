package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.evensel.swyftr.MainActivity;
import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ResponseModel;
import com.evensel.swyftr.util.ValidatorUtil;

/**
 * Created by Prishan Maduka on 2/11/2017.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView txtForgotPassword,txtSignUp;
    private EditText txtUserName,txtPassword;
    private Button btnLogin;
    private ImageView btnFacebook,btnTwitter,btnGooglePlus;
    private LayoutInflater inflate;
    private View layout;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing UI components
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);
        txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnFacebook = (ImageView)findViewById(R.id.btnFacebook);
        btnTwitter = (ImageView)findViewById(R.id.btnTwitter);
        btnGooglePlus = (ImageView)findViewById(R.id.btnGoogle);

        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

        //Assigning click listeners
        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnTwitter.setOnClickListener(this);
        btnGooglePlus.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtForgotPassword){
            Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(intent);
        }else if(view.getId()==R.id.txtSignUp){
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        }else if(view.getId()==R.id.btnLogin){
            if(txtUserName.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Username cannot be empty.").show();
            }else if(!ValidatorUtil.isValidEmailAddress(txtUserName.getText().toString())){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Invalid email address.").show();
            }else if(txtPassword.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Password cannot be empty.").show();
            }else{
                String message = ValidatorUtil.isValidPassword(txtPassword.getText().toString());
                if(!message.equalsIgnoreCase("Success")){
                    Notifications.showToastMessage(layout,getApplicationContext(),message).show();
                }else{
                    progress = ProgressDialog.show(LoginActivity.this, null,
                            "Authenticating...", true);
                    JsonRequestManager.getInstance(LoginActivity.this).loginUserRequest(AppURL.APPLICATION_BASE_URL+AppURL.USER_LOGIN_URL, txtUserName.getText().toString(),txtPassword.getText().toString(), requestCallback);
                }
            }
        }else if(view.getId()==R.id.btnFacebook){

        }else if(view.getId()==R.id.btnTwitter){

        }else if(view.getId()==R.id.btnGoogle){

        }
    }

    //Response callback for "User Login"
    private final JsonRequestManager.loginUser requestCallback = new JsonRequestManager.loginUser() {

        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.LOGIN_SHARED_PREF_USERNAME, txtUserName.getText().toString());
                editor.putString(Constants.LOGIN_SHARED_PREF_PASSWORD, txtPassword.getText().toString());
                editor.putString(Constants.LOGIN_ACCESS_TOKEN, model.getToken());
                editor.commit();
                logUser();
            }else{
                Notifications.showToastMessage(layout,getApplicationContext(),model.getMessage()).show();
            }


        }

        @Override
        public void onError(ResponseModel model) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getApplicationContext(),model.getMessage()).show();
        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getApplicationContext(),status).show();
        }
    };

    private void logUser(){
        Intent loggedIntent = new Intent(this, MainActivity.class);
        startActivity(loggedIntent);
    }
}
