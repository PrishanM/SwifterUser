package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ResponseModel;
import com.evensel.swyftr.util.ValidatorUtil;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class ResetPasswordActivity extends Activity implements View.OnClickListener {

    private Button btnDone;
    private EditText txtPassword,txtRepeatPassword;
    private LayoutInflater inflate;
    private View layout;
    private ProgressDialog progress;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_new_password);

        //Initialize UI components
        context = this;
        txtPassword = (EditText)findViewById(R.id.txtNewPassword);
        txtRepeatPassword = (EditText)findViewById(R.id.txtReType);
        btnDone = (Button)findViewById(R.id.btnReset);

        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

        //Assign click listeners
        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnReset){
            if(txtPassword.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Password cannot be empty.").show();
            }else{
                String message = ValidatorUtil.isValidPassword(txtPassword.getText().toString());
                if(!message.equalsIgnoreCase("Success")){
                    Notifications.showToastMessage(layout,getApplicationContext(),message).show();
                }else{
                    if(txtPassword.getText().toString().equalsIgnoreCase(txtRepeatPassword.getText().toString())){
                        progress = ProgressDialog.show(context, null,
                                "Resetting Password...", true);
                        JsonRequestManager.getInstance(context).testRequest(AppURL.APPLICATION_BASE_URL+AppURL.RESET_PASSWORD_URL,getIntent().getStringExtra("EMAIL"), txtPassword.getText().toString(),txtRepeatPassword.getText().toString(), requestCallback);
                    }else{
                        Notifications.showToastMessage(layout,getApplicationContext(),"Repeat password does not match.").show();
                    }
                }


            }
        }
    }

    //Response callback for "Reset Password"
    private final JsonRequestManager.test requestCallback = new JsonRequestManager.test() {

        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
                alertDialog.setMessage("Password reset successfully");
                // Setting OK Button
                alertDialog.setButton("Back to login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
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

}
