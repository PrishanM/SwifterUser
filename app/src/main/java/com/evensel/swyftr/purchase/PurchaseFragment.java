package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by Prishan Maduka on 3/05/2017.
 */
public class PurchaseFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PurchaseItemPagerAdapter purchaseItemPagerAdapter;
    private SearchItemPagerAdapter searchItemPagerAdapter;
    private ImageView imgHome,imgLocation;
    private EditText edtSearch;

    private ProgressDialog progress;
    private LayoutInflater inflate;
    private View layout;
    private String token = "";
    private final Handler handler = new Handler();
    private Runnable runnable;
    private ArrayList<Datum> datumArrayList = new ArrayList<>();
    private ArrayList<Datum> searchList = new ArrayList<>();


    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_purchase_base, container, false);

        viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
        edtSearch = (EditText)rootView.findViewById(R.id.txtSearch);
        imgHome = (ImageView)rootView.findViewById(R.id.imgHome);

        inflate = inflater;
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) rootView.findViewById(R.id.toast_layout_root));
        SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.LOGIN_ACCESS_TOKEN, "");


        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchData(edtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        addFiles();

        imgHome.setOnClickListener(this);

        return rootView;
    }

    private void performSearch() {
        viewPager.getCurrentItem();
        viewPager.setAdapter(null);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Foodcity");
        /*titles.add("Grocerymart");
        titles.add("Laugfs");*/
        searchItemPagerAdapter =
                new SearchItemPagerAdapter(getChildFragmentManager(),titles);
        viewPager.setAdapter(searchItemPagerAdapter);
        imgHome.setVisibility(View.VISIBLE);

    }

    private void setupViewPager() {

        ArrayList<String> titles = new ArrayList<>();
        titles.add("Foodcity");
        /*titles.add("Grocerymart");
        titles.add("Laugfs");*/

        purchaseItemPagerAdapter =
                new PurchaseItemPagerAdapter(getChildFragmentManager(),titles);
        viewPager.setAdapter(purchaseItemPagerAdapter);


    }

    private void addFiles() {

        progress = ProgressDialog.show(getActivity(), null,
                "Loading...", true);
        JsonRequestManager.getInstance(getActivity()).getCategories(AppURL.APPLICATION_BASE_URL+AppURL.GET_CATEGORY_LIST+"?page=1",token, getCategoriesRequest);
    }

    private void searchData(String parameter){
        AppController.setSearchArrayList(null);
        progress = ProgressDialog.show(getActivity(), null,
                "Loading...", true);
        JsonRequestManager.getInstance(getActivity()).productSearchHome(AppURL.APPLICATION_BASE_URL+AppURL.PRODUCT_HOME_SEARCH+parameter+"?page=1",token, productSearchHomeRequest);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imgHome){
            setupViewPager();
            imgHome.setVisibility(View.GONE);
        }
    }

    //Response callback for "Get category List"
    private final JsonRequestManager.getCategoriesRequest getCategoriesRequest = new JsonRequestManager.getCategoriesRequest() {
        @Override
        public void onSuccess(CategoriesResponse model) {

            if(model.getStatus().equalsIgnoreCase("success")){
                for(Datum datum :model.getDetails().getData()){
                    datumArrayList.add(datum);
                }
                if(model.getDetails().getNextPageUrl()!=null)
                    getNextList(model.getDetails().getNextPageUrl());
                else{
                    if(progress!=null)
                        progress.dismiss();
                    AppController.setDatumArrayList(datumArrayList);
                    setupViewPager();
                }
            }else{
                Notifications.showToastMessage(layout,getActivity(),model.getMessage()).show();
            }
        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getActivity(),status).show();
        }

        @Override
        public void onError(CategoriesResponse model) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getActivity(),model.getMessage()).show();
        }
    };

    private void getNextList(final String url) {
        runnable = new Runnable() {
            @Override
            public void run() {
                JsonRequestManager.getInstance(getActivity()).getCategories(url,token, getCategoriesRequest);
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    private void getNextSearchList(final String url) {
        runnable = new Runnable() {
            @Override
            public void run() {
                JsonRequestManager.getInstance(getActivity()).productSearchHome(url,token, productSearchHomeRequest);
            }
        };

        handler.postDelayed(runnable, 3000);
    }


    //Response callback for "Search List"
    private final JsonRequestManager.productSearchHomeRequest productSearchHomeRequest = new JsonRequestManager.productSearchHomeRequest() {
        @Override
        public void onSuccess(CategoriesResponse model) {

            if(model.getStatus().equalsIgnoreCase("success")){
                for(Datum datum :model.getDetails().getData()){
                    searchList.add(datum);
                }
                if(model.getDetails().getNextPageUrl()!=null)
                    getNextSearchList(model.getDetails().getNextPageUrl());
                else{
                    if(progress!=null)
                        progress.dismiss();
                    AppController.setSearchArrayList(searchList);
                    performSearch();
                }
            }else{
                Notifications.showToastMessage(layout,getActivity(),model.getMessage()).show();
            }
        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getActivity(),status).show();
        }

        @Override
        public void onError(CategoriesResponse model) {
            if(progress!=null)
                progress.dismiss();
            Notifications.showToastMessage(layout,getActivity(),model.getMessage()).show();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }



}
