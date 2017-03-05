package com.evensel.swyftr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.evensel.swyftr.authentication.LoginActivity;
import com.evensel.swyftr.deliveries.ActiveDeliveriesFragment;
import com.evensel.swyftr.profile.ProfileFragment;
import com.evensel.swyftr.purchase.PurchaseFragment;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.Constants;
import com.mikhaellopez.circularimageview.CircularImageView;

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

        SharedPreferences sharedPref = getSharedPreferences(Constants.PROFILE_PREF, Context.MODE_PRIVATE);
        String username = sharedPref.getString(Constants.PROFILE_NAME, "");
        String imageUrl = sharedPref.getString(Constants.PROFILE_PIC_URL, "");
        String imageUri = sharedPref.getString(Constants.PROFILE_PIC,"");

        TextView userData = (TextView)findViewById(R.id.name);
        final CircularImageView imageView = (CircularImageView)findViewById(R.id.imgUser);
        if(username.isEmpty()){
            userData.setText("Edit Name");
        }else{
            userData.setText(username);
        }

        if(!imageUrl.isEmpty()){
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            int time = (int) (System.currentTimeMillis());//gets the current time in milliseconds
            String myUrl = imageUrl+"?timestamp=" + String.valueOf(time);
            imageLoader.get(myUrl, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERR", "Image Load Error: " + error.getMessage());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }
            });
        }else if(!imageUri.isEmpty()){
            Matrix matrix = new Matrix();
            matrix.postRotate(AppController.getImageOrientation(imageUri));
            Bitmap rotatedBitmap = Bitmap.createBitmap(profileImage(imageUri), 0, 0, profileImage(imageUri).getWidth(),
                    profileImage(imageUri).getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }


        fragmentNo = getIntent().getIntExtra("FRAGMENT", 0);

        // display the first navigation drawer view on app launch
        displayView(fragmentNo);


    }

    private Bitmap profileImage(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap realPhoto = BitmapFactory.decodeFile(path, options);
        return realPhoto;
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
                title = getString(R.string.nav_purchase);
                fragment = new PurchaseFragment();

                break;
            case 1:
                title = getString(R.string.nav_active_deliveries);
                fragment = new ActiveDeliveriesFragment();

                break;
            case 2:
                title = getString(R.string.nav_delivery_history);
                fragment = new ActiveDeliveriesFragment();
                break;
            case 3:
                title = getString(R.string.nav_invite_friends);
                fragment = new ActiveDeliveriesFragment();

                break;
            case 4:
                title = getString(R.string.nav_schedule_basket);
                fragment = new ActiveDeliveriesFragment();
                break;
            case 5:
                title = getString(R.string.nav_profile_settings);
                fragment = new ProfileFragment();
                break;
            case 6:
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

                        SharedPreferences profilePref = context.getSharedPreferences(Constants.PROFILE_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor profileEditor = profilePref.edit();
                        profileEditor.clear();
                        profileEditor.commit();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().getRequestQueue().getCache().clear();
    }
}
