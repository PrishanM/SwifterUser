package com.evensel.swyftr.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class ForgotPasswordSendResetCodeActivity extends Activity implements View.OnClickListener {

    private TextView txtResend;
    private Button btnReset;
    private EditText txtResetCode;
    private LayoutInflater inflate;
    private View layout;
    private ProgressDialog progress;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_code);

        //Initialize UI components
        context = this;
        txtResend = (TextView)findViewById(R.id.txtResend);
        txtResetCode = (EditText)findViewById(R.id.txtResetCode);
        btnReset = (Button)findViewById(R.id.btnReset);

        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

        //Assign click listeners
        txtResend.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtResend){
            finish();
        }else if(view.getId()==R.id.btnReset){
            if(txtResetCode.getText().toString().isEmpty()){
                Notifications.showToastMessage(layout,getApplicationContext(),"Sorry!!! Reset code cannot be empty.").show();
            }else{
                progress = ProgressDialog.show(context, null,
                        "Sending...", true);
                JsonRequestManager.getInstance(context).validateResetCodeRequest(AppURL.APPLICATION_BASE_URL+AppURL.VALIDATE_RESET_CODE_URL, txtResetCode.getText().toString(), requestCallback);

            }
        }
    }

    //Response callback for "Validate Code"
    private final JsonRequestManager.validateResetCode requestCallback = new JsonRequestManager.validateResetCode() {

        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordSendResetCodeActivity.this).create();
                alertDialog.setTitle("SENT");
                // Setting Dialog Message
                alertDialog.setMessage("Password reset code has been validated successfully");
                // Setting OK Button
                alertDialog.setButton("Set new password", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
