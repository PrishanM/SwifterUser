package com.evensel.swyftr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.evensel.swyftr.authentication.LoginActivity;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.DetectApplicationFunctionsAvailability;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.LoginResponse;

public class IntroActivity extends Activity {

    private Context context;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        context = this;
        TextView txtVideo = (TextView)findViewById(R.id.txtVideo);
        startHandler();
        txtVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                startLogin();
            }
        });

    }

    private void startHandler() {
        int SPLASH_TIMEOUT_TIME = 3000;
        runnable = new Runnable() {
            @Override
            public void run() {
                startLogin();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIMEOUT_TIME);
    }

    //Response callback for "User Login"
    private final JsonRequestManager.loginUser requestCallback = new JsonRequestManager.loginUser() {

        @Override
        public void onSuccess(LoginResponse model) {

            if(progressDialog!=null)
                progressDialog.dismiss();
            if(model.getStatus().equalsIgnoreCase("success")){
                SharedPreferences sharedPref =context.getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.LOGIN_ACCESS_TOKEN, model.getToken());
                editor.commit();
                AppController.setProfilePreference(context,model.getDetails());

                Intent loggedIntent = new Intent(context, MainActivity.class);
                startActivity(loggedIntent);
                finish();
            }else{
                navigateLogin();
            }


        }

        @Override
        public void onError(LoginResponse model) {
            if(progressDialog!=null)
                progressDialog.dismiss();
            navigateLogin();
        }

        @Override
        public void onError(String status) {
            if(progressDialog!=null)
                progressDialog.dismiss();
            navigateLogin();
        }
    };

    private void navigateLogin(){
        Intent mainActivity = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void startLogin(){

        //Start next activity
        SharedPreferences sharedPref = getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        String username = sharedPref.getString(Constants.LOGIN_SHARED_PREF_USERNAME, "");
        String password = sharedPref.getString(Constants.LOGIN_SHARED_PREF_PASSWORD, "");

        if(username!=null && !username.isEmpty() && password!=null && !password.isEmpty()){
            DetectApplicationFunctionsAvailability.setmContext(context);
            if(!DetectApplicationFunctionsAvailability.isConnected()){
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                // Setting Dialog Message
                alertDialog.setMessage(getResources().getString(R.string.no_network_error));
                // Setting OK Button
                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
                //Notifications.showGeneralDialog(context,getResources().getString(R.string.no_network_error)).show();
            }else{
                progressDialog = ProgressDialog.show(this, null,
                        "Loading...", true);
                JsonRequestManager.getInstance(context).loginUserRequest(AppURL.APPLICATION_BASE_URL+AppURL.USER_LOGIN_URL, username,password, requestCallback);
            }

        }else{
            navigateLogin();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
