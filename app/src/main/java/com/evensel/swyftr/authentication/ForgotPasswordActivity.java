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
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;
import com.evensel.swyftr.util.ResponseModel;
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
    private ProgressDialog progress;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Initialize UI components
        context = this;
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
                progress = ProgressDialog.show(context, null,
                        "Authenticating...", true);
                JsonRequestManager.getInstance(context).sendResetCodeRequest(AppURL.APPLICATION_BASE_URL+AppURL.SEND_RESET_CODE_URL, txtEmailAddress.getText().toString(), requestCallback);

            }
        }
    }

    //Response callback for "User Login"
    private final JsonRequestManager.sendResetCode requestCallback = new JsonRequestManager.sendResetCode() {

        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                alertDialog.setTitle("SENT");
                // Setting Dialog Message
                alertDialog.setMessage("Password reset code has been sent successfully");
                // Setting OK Button
                alertDialog.setButton("Enter code now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,ForgotPasswordSendResetCodeActivity.class);
                        intent.putExtra("EMAIL",txtEmailAddress.getText().toString());
                        startActivity(intent);
                        //finish();
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
