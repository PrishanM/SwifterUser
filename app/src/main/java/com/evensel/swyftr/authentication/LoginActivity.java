package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.evensel.swyftr.MainActivity;
import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.DetectApplicationFunctionsAvailability;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.LoginResponse;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ValidatorUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        facebookSDKInitialize();

        //Initializing UI components
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);
        txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnFacebook = (ImageView)findViewById(R.id.btnFacebook);
        btnTwitter = (ImageView)findViewById(R.id.btnTwitter);
        btnGooglePlus = (ImageView)findViewById(R.id.btnGoogle);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions("email");
        getLoginDetails(loginButton);

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

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    protected void getLoginDetails(LoginButton login_button) {
    // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess (LoginResult loginResult){
                Log.d("xxxxx",loginResult.getAccessToken()
                        .getToken());
            }
            @Override
            public void onCancel () {
                Log.d("xxxxx","vccvcv");
            }
            @Override
            public void onError (FacebookException exception){
                Log.d("xxxxx",exception.getMessage());
            }
        });
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
                    DetectApplicationFunctionsAvailability.setmContext(LoginActivity.this);
                    if(!DetectApplicationFunctionsAvailability.isConnected()){
                        Notifications.showGeneralDialog(LoginActivity.this,getResources().getString(R.string.no_network_error)).show();
                    }else{
                        progress = ProgressDialog.show(LoginActivity.this, null,
                                "Authenticating...", true);
                        JsonRequestManager.getInstance(LoginActivity.this).loginUserRequest(AppURL.APPLICATION_BASE_URL+AppURL.USER_LOGIN_URL, txtUserName.getText().toString(),txtPassword.getText().toString(), requestCallback);
                    }

                }
            }
        }else if(view.getId()==R.id.btnFacebook){
            loginButton.performClick();
        }else if(view.getId()==R.id.btnTwitter){

        }else if(view.getId()==R.id.btnGoogle){

        }
    }

    //Response callback for "User Login"
    private final JsonRequestManager.loginUser requestCallback = new JsonRequestManager.loginUser() {

        @Override
        public void onSuccess(LoginResponse model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                AppController.setLoginPreference(LoginActivity.this,txtUserName.getText().toString(),txtPassword.getText().toString(),model.getToken());
                AppController.setProfilePreference(LoginActivity.this,model.getDetails());
                logUser();
            }else{
                Notifications.showToastMessage(layout,getApplicationContext(),model.getMessage()).show();
            }


        }

        @Override
        public void onError(LoginResponse model) {
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
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }


}
