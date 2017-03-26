package com.evensel.swyftr.deliveries;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Prishan Maduka on 3/19/2017.
 */
public class DeliveryOptionsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private TextView txtETA,txtDC,txtTotal,txtDate,txtTime;
    private Button btnNow,btnSchedule,btnCheckout,btnDate,btnTime;
    private ImageView imgStandard, imgPremium,imgAccept,imgLocation;
    private LinearLayout layoutNow,layoutDateTime;
    private Switch switchPersonal;
    private boolean isAccepted = false,isPersonal=false;
    private int deliveryMode = 1;
    private GoogleMap googleMap;
    private double currentLatitude, currentLongitude;
    private SupportMapFragment mapFragment;
    private FragmentManager myFragmentManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 140;
    private LatLng parameterLatLang;
    private ProgressDialog progress;

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
        imgLocation = (ImageView)findViewById(R.id.imgLocation);

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
                    final boolean[] chkAccepted = {false};

                    final Dialog dialogFriend = new Dialog(DeliveryOptionsActivity.this);
                    dialogFriend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogFriend.setContentView(R.layout.custom_friend_receieve_dialog);
                    dialogFriend.show();

                    dialogFriend.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });

                    final ImageView checkBox = (ImageView)dialogFriend.findViewById(R.id.checkBtn);
                    Button btnOk = (Button)dialogFriend.findViewById(R.id.btnDone);
                    Button btnCancel = (Button)dialogFriend.findViewById(R.id.btnCancel);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogFriend.dismiss();
                    /*progress = ProgressDialog.show(DeliveryOptionsActivity.this, null,
                            "Loading...", true);*/
                            //JsonRequestManager.getInstance(DeliveryOptionsActivity.this).updateLocation(AppURL.APPLICATION_BASE_URL+AppURL.UPDATE_LOCATION,token,parameterLatLang.longitude,parameterLatLang.latitude, locationResponseCallback);
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogFriend.dismiss();
                        }
                    });

                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(chkAccepted[0]){
                                chkAccepted[0] = false;
                                checkBox.setImageResource(R.drawable.check_box);
                            }else{
                                chkAccepted[0] = true;
                                checkBox.setImageResource(R.drawable.check_box_selected);
                            }
                        }
                    });
                }
            }
        });
        imgStandard.setOnClickListener(this);
        imgPremium.setOnClickListener(this);
        imgAccept.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
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
        }else if(view.getId()==R.id.imgLocation){
            final Dialog dialog1 = new Dialog(DeliveryOptionsActivity.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.custom_map);
            dialog1.show();
            setMap();

            dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(mapFragment.getView()!=null){
                        myFragmentManager.beginTransaction().remove(mapFragment).commit();

                    }
                }
            });

            Button btnOk = (Button)dialog1.findViewById(R.id.btnOk);
            Button btnCancel = (Button)dialog1.findViewById(R.id.btnCancel);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                    /*progress = ProgressDialog.show(DeliveryOptionsActivity.this, null,
                            "Loading...", true);*/
                    //JsonRequestManager.getInstance(DeliveryOptionsActivity.this).updateLocation(AppURL.APPLICATION_BASE_URL+AppURL.UPDATE_LOCATION,token,parameterLatLang.longitude,parameterLatLang.latitude, locationResponseCallback);
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });

        }else if(view.getId()==R.id.btnCheckout){

        }else if(view.getId()==R.id.btnDatePick){
            showDatePicker();
        }else if(view.getId()==R.id.btnTimePick){
            showTimePicker();
        }
    }

    private void setMap() {
        myFragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) myFragmentManager
                .findFragmentById(R.id.mapView);
        MapsInitializer.initialize(DeliveryOptionsActivity.this);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                parameterLatLang = latLng;
                addMarkers(latLng);
            }
        });

        enableLocation(googleMap);


    }

    private void enableLocation(GoogleMap googleMap) {
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(DeliveryOptionsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DeliveryOptionsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }else{
                googleMap.setMyLocationEnabled(true);
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                Location l;

                for (int i = 0; i < providers.size(); i++) {
                    l = lm.getLastKnownLocation(providers.get(i));
                    if (l != null) {
                        currentLatitude = l.getLatitude();
                        currentLongitude = l.getLongitude();
                        break;
                    }
                }
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(currentLatitude, currentLongitude)).title("My Location").snippet("");
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(currentLatitude, currentLongitude)).zoom(15).build();

                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                googleMap.addMarker(marker);
            }

        }

    }

    private void addMarkers(LatLng latLng){
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Address"));
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
