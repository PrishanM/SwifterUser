package com.evensel.swyftr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.evensel.swyftr.authentication.LoginActivity;
import com.evensel.swyftr.deliveries.ActiveDeliveriesFragment;
import com.evensel.swyftr.profile.ProfileFragment;
import com.evensel.swyftr.util.Constants;

/**
 * Created by Prishan Maduka on 2/11/2017.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private Toolbar toolbar;
    private View prevView;
    private Context context;
    private int fragmentNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        context = this;

        TextView userData = (TextView)findViewById(R.id.name);
        userData.setText("Thilina Senanayake");

        fragmentNo = getIntent().getIntExtra("FRAGMENT", 0);

        // display the first navigation drawer view on app launch
        displayView(fragmentNo);


    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
        view.setActivated(true);
        if (prevView != null) {
            prevView.setActivated(false);
        }
        prevView = view;
    }

    private void displayView(int position) {
        Fragment fragment = null;
        android.app.Fragment prefFrag = null;

        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                title = getString(R.string.nav_active_deliveries);
                fragment = new ActiveDeliveriesFragment();

                break;
            case 1:
                title = getString(R.string.nav_delivery_history);
                fragment = new ActiveDeliveriesFragment();
                break;
            case 2:
                title = getString(R.string.nav_invite_friends);
                fragment = new ActiveDeliveriesFragment();

                break;
            case 3:
                title = getString(R.string.nav_schedule_basket);
                fragment = new ActiveDeliveriesFragment();
                break;
            case 4:
                title = getString(R.string.nav_profile_settings);
                fragment = new ProfileFragment();
                break;
            case 5:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Are you sure want to logout?");
                alertDialog.setButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                // Setting OK Button
                alertDialog.setButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences loginPref = context.getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor loginEditor = loginPref.edit();
                        loginEditor.clear();
                        loginEditor.commit();

                        Intent mainActivity = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(mainActivity);
                        finish();
                    }
                });

                alertDialog.show();

                break;
            default:
                break;
        }



        if (fragment != null) {
            setFragment(fragment,title);
        }
    }

    private void setFragment(Fragment fragment,String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
    }
}
