package com.evensel.swyftr.purchase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;

/**
 * Created by Prishan Maduka on 3/14/2017.
 */
public class CategoryProduct extends AppCompatActivity implements View.OnClickListener {

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
    private EditText edtSearch;
    private Button btnBrand,btnType;
    private String brandName = "";
    private int brandId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_items_base);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("PRODUCT")+" Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.LOGIN_ACCESS_TOKEN, "");
        context = this;
        inflate = getLayoutInflater();
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup)findViewById(R.id.toast_layout_root));
        edtSearch = (EditText)findViewById(R.id.txtSearch);
        btnBrand = (Button)findViewById(R.id.txtBrand);
        btnType = (Button)findViewById(R.id.txtType);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!edtSearch.getText().toString().isEmpty())
                        searchData(edtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        btnBrand.setOnClickListener(this);
        btnType.setOnClickListener(this);


        addFiles();

    }

    private void searchData(String s) {
        progress = ProgressDialog.show(this, null,
                "Loading...", true);
        String url = "";
        if(btnBrand.getText().toString().equalsIgnoreCase("Select Brand"))
            url = AppURL.APPLICATION_BASE_URL+AppURL.CATEGORY_SEARCH_URL+getIntent().getIntExtra("ID",1)+"/"+s+"?page=1";
        else
            url = AppURL.APPLICATION_BASE_URL+AppURL.CATEGORY_SEARCH_URL+getIntent().getIntExtra("ID",1)+"/"+brandId+"/"+s+"?page=1";
        JsonRequestManager.getInstance(this).categoryBasedSearch(url,token, categorySearchRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_other, menu);
        final MenuItem item = menu.findItem(R.id.idDone);
        TextView txtPrice = (TextView)menu.findItem(R.id.idDone).getActionView().findViewById(R.id.txtPrice);
        txtPrice.setText("LKR "+ AppController.getAmount());

        item.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryProduct.this, CartStepOneActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

    private void setupViewPager() {
        viewPager.setAdapter(null);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Wine World");

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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtBrand){
            progress = ProgressDialog.show(this, null,
                    "Loading...", true);
            JsonRequestManager.getInstance(this).getBrandList(AppURL.APPLICATION_BASE_URL+AppURL.CATEGORY_BRANDS_URL+getIntent().getIntExtra("ID",1),getBrandList);
        }
    }

    //Response callback for "Get Brand List"
    private final JsonRequestManager.getBrandListRequest getBrandList = new JsonRequestManager.getBrandListRequest() {
        @Override
        public void onSuccess(CategoriesResponse model) {

            if(progress!=null)
                progress.dismiss();

            if(model.getStatus().equalsIgnoreCase("success")){
                showBrandListDialog(model.getDetails().getData());
                /*for(Datum datum :model.getDetails().getData()){
                    brandMap.put(datum.getId(),datum.getBrandName());
                }*/
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

    private void showBrandListDialog(final List<Datum> datumList){
        final ArrayList<String> names = new ArrayList<>();
        for(Datum datum :datumList){
            names.add(datum.getBrandName());
        }
        String[] stockArr = new String[names.size()];
        stockArr = names.toArray(stockArr);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_alert_dialog, null);
        alertDialog.setView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stockArr);
        lv.setAdapter(adapter);

        final AlertDialog dialog = alertDialog.create();

        dialog.show();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brandName = names.get(position);
                brandId = datumList.get(position).getId();
                btnBrand.setText(brandName);
                dialog.dismiss();
            }
        });
    }

    //Response callback for "Get category List"
    private final JsonRequestManager.categoryBasedSearchRequest categorySearchRequest = new JsonRequestManager.categoryBasedSearchRequest() {
        @Override
        public void onSuccess(CategoriesResponse model) {

            if(progress!=null)
                progress.dismiss();
            datumArrayList.clear();

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
            datumArrayList.clear();
        }

        @Override
        public void onError(CategoriesResponse model) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,context,model.getMessage()).show();
            datumArrayList.clear();
        }
    };
}
