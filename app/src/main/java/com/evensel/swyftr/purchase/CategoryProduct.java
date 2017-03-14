package com.evensel.swyftr.purchase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.CategoriesResponse;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.Datum;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;

import java.util.ArrayList;

/**
 * Created by Prishan Maduka on 3/14/2017.
 */
public class CategoryProduct extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchItemPagerAdapter searchItemPagerAdapter;
    private ArrayList<Datum> datumArrayList = new ArrayList<>();
    private ProgressDialog progress;
    private String token = "";
    private LayoutInflater inflate;
    private View layout;
    private Context context;
    private final Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_items_base);

        SharedPreferences sharedPref = getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.LOGIN_ACCESS_TOKEN, "");
        context = this;
        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup)findViewById(R.id.toast_layout_root));
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        addFiles();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_other, menu);
        TextView txtPrice = (TextView)menu.findItem(R.id.idDone).getActionView().findViewById(R.id.txtPrice);
        txtPrice.setText("LKR "+ AppController.getAmount());
        return true;
    }

    private void setupViewPager() {
        viewPager.setAdapter(null);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Foodcity");

        searchItemPagerAdapter =
                new SearchItemPagerAdapter(getSupportFragmentManager(),titles);
        viewPager.setAdapter(searchItemPagerAdapter);


    }

    private void addFiles() {

        progress = ProgressDialog.show(this, null,
                "Loading...", true);
        JsonRequestManager.getInstance(this).categoryProducts(AppURL.APPLICATION_BASE_URL+AppURL.CATEGORY_PRODUCTS_URL+getIntent().getIntExtra("ID",1)+"?page=1",token, getCategoriesRequest);
    }

    //Response callback for "Get category List"
    private final JsonRequestManager.categoryProductRequest getCategoriesRequest = new JsonRequestManager.categoryProductRequest() {
        @Override
        public void onSuccess(CategoriesResponse model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                for(Datum datum :model.getDetails().getData()){
                    datumArrayList.add(datum);
                }
                AppController.setSearchArrayList(datumArrayList);
                setupViewPager();
                if(model.getDetails().getNextPageUrl()!=null)
                    getNextList(model.getDetails().getNextPageUrl());
            }else{
                Notifications.showToastMessage(layout,context,model.getMessage()).show();
            }
        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,context,status).show();
        }

        @Override
        public void onError(CategoriesResponse model) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,context,model.getMessage()).show();
        }
    };

    private void getNextList(final String url) {
        runnable = new Runnable() {
            @Override
            public void run() {
                JsonRequestManager.getInstance(context).categoryProducts(url,token, getCategoriesRequest);
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
