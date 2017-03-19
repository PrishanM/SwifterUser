package com.evensel.swyftr.deliveries;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.DatePickerCustom;
import com.evensel.swyftr.util.TimePickerCustom;

import java.util.Calendar;

/**
 * Created by Prishan Maduka on 3/19/2017.
 */
public class DeliveryOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtETA,txtDC,txtTotal,txtDate,txtTime;
    private Button btnNow,btnSchedule,btnCheckout,btnDate,btnTime;
    private ImageView imgStandard, imgPremium,imgAccept;
    private LinearLayout layoutNow,layoutDateTime;
    private Switch switchPersonal;
    private boolean isAccepted = false,isPersonal=false;
    private int deliveryMode = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Delivery Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtETA = (TextView)findViewById(R.id.txtETA);
        txtDC = (TextView)findViewById(R.id.txtDC);
        txtTotal = (TextView)findViewById(R.id.txtTotal);
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtTime = (TextView)findViewById(R.id.txtTime);

        txtTotal.setText("TOTAL : "+ (AppController.getAmount()+230)+" LKR");

        imgStandard = (ImageView)findViewById(R.id.btnStandard);
        imgPremium = (ImageView)findViewById(R.id.btnPremium);
        imgAccept = (ImageView)findViewById(R.id.checkAccept);

        btnNow = (Button)findViewById(R.id.btnNow);
        btnSchedule = (Button)findViewById(R.id.btnSchedule);
        btnCheckout = (Button)findViewById(R.id.btnCheckout);
        btnDate = (Button)findViewById(R.id.btnDatePick);
        btnTime = (Button)findViewById(R.id.btnTimePick);

        layoutDateTime = (LinearLayout) findViewById(R.id.layoutDateTime);
        layoutNow = (LinearLayout)findViewById(R.id.layoutNow);

        switchPersonal = (Switch)findViewById(R.id.switchPersonal);

        switchPersonal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isPersonal=true;
                }else{
                    isPersonal = false;
                }
            }
        });
        imgStandard.setOnClickListener(this);
        imgPremium.setOnClickListener(this);
        imgAccept.setOnClickListener(this);
        btnNow.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnStandard){
            deliveryMode = 1;
            imgStandard.setImageResource(R.drawable.standard_selected_btn);
            imgPremium.setImageResource(R.drawable.premium_btn);
        }else if(view.getId()==R.id.btnPremium){
            deliveryMode = 2;
            imgStandard.setImageResource(R.drawable.standard_btn);
            imgPremium.setImageResource(R.drawable.premium_selected_btn);
        }else if(view.getId()==R.id.checkAccept){
            if(isAccepted){
                isAccepted = false;
                imgAccept.setImageResource(R.drawable.check_box);
            }else{
                isAccepted = true;
                imgAccept.setImageResource(R.drawable.check_box_selected);
            }
        }else if(view.getId()==R.id.btnNow){
            txtETA.setVisibility(View.VISIBLE);
            txtDC.setGravity(Gravity.RIGHT);
            layoutDateTime.setVisibility(View.GONE);
            layoutNow.setVisibility(View.VISIBLE);

        }else if(view.getId()==R.id.btnSchedule){
            txtETA.setVisibility(View.GONE);
            txtDC.setGravity(Gravity.LEFT);
            layoutDateTime.setVisibility(View.VISIBLE);
            layoutNow.setVisibility(View.GONE);
        }else if(view.getId()==R.id.btnCheckout){

        }else if(view.getId()==R.id.btnDatePick){
            showDatePicker();
        }else if(view.getId()==R.id.btnTimePick){
            showTimePicker();
        }
    }

    private void showDatePicker() {
        DatePickerCustom date = new DatePickerCustom();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("YEAR", calender.get(Calendar.YEAR));
        args.putInt("MONTH", calender.get(Calendar.MONTH));
        args.putInt("DAY", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(regDateListener);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    private void showTimePicker() {
        TimePickerCustom time = new TimePickerCustom();
        /**
         * Set Up Current Time Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("HOUR_OF_DAY", calender.get(Calendar.HOUR_OF_DAY));
        args.putInt("MINUTE", calender.get(Calendar.MINUTE));
        time.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        time.setCallBack(regTimeListener);
        time.show(getSupportFragmentManager(), "Time Picker");
    }

    private final DatePickerDialog.OnDateSetListener regDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            txtDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

        }
    };

    private final TimePickerDialog.OnTimeSetListener regTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            txtTime.setText(hourOfDay+":"+minute);
        }
    };
}
