package com.evensel.swyftr.purchase;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.CategoriesResponse;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.Datum;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.LocationResponseModel;
import com.evensel.swyftr.util.Notifications;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prishan Maduka on 3/05/2017.
 */
public class PurchaseFragment extends Fragment implements OnMapReadyCallback,View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PurchaseItemPagerAdapter purchaseItemPagerAdapter;
    private SearchItemPagerAdapter searchItemPagerAdapter;
    private ImageView imgHome,imgLocation,imgBack;
    private LinearLayout layoutSearch,layoutLocation;
    private EditText edtSearch;
    private TextView txtAddress,txtChange;
    private SharedPreferences profilePref;

    private ProgressDialog progress;
    private LayoutInflater inflate;
    private View layout;
    private String token = "";
    private final Handler handler = new Handler();
    private Runnable runnable;
    private ArrayList<Datum> datumArrayList = new ArrayList<>();
    private ArrayList<Datum> searchList = new ArrayList<>();
    private GoogleMap googleMap;
    private double currentLatitude, currentLongitude;
    private SupportMapFragment mapFragment;
    private FragmentManager myFragmentManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 140;
    private LatLng parameterLatLang;


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
        imgLocation = (ImageView)rootView.findViewById(R.id.imgLocation);
        imgBack = (ImageView)rootView.findViewById(R.id.imgBack);

        layoutSearch = (LinearLayout)rootView.findViewById(R.id.nav_header_container);
        layoutLocation = (LinearLayout)rootView.findViewById(R.id.nav_header_location);

        profilePref = getActivity().getSharedPreferences(Constants.PROFILE_PREF, Context.MODE_PRIVATE);

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
        txtAddress = (TextView)rootView.findViewById(R.id.txtAddress);
        txtChange = (TextView)rootView.findViewById(R.id.txtChange);
        txtAddress.setText(profilePref.getString(Constants.PROFILE_ADDRESS, ""));

        imgHome.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtChange.setOnClickListener(this);

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
            searchList.clear();
            setupViewPager();
            imgHome.setVisibility(View.GONE);
        }else if(v.getId()==R.id.imgLocation){
            layoutSearch.setVisibility(View.GONE);
            layoutLocation.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.imgBack){
            layoutSearch.setVisibility(View.VISIBLE);
            layoutLocation.setVisibility(View.GONE);
        }else if(v.getId()==R.id.txtChange){
            final Dialog dialog1 = new Dialog(getActivity());
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
                    progress = ProgressDialog.show(getActivity(), null,
                            "Loading...", true);
                    JsonRequestManager.getInstance(getActivity()).updateLocation(AppURL.APPLICATION_BASE_URL+AppURL.UPDATE_LOCATION,token,parameterLatLang.longitude,parameterLatLang.latitude, locationResponseCallback);
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
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

    private void setMap() {
        myFragmentManager = getActivity().getSupportFragmentManager();
        mapFragment = (SupportMapFragment) myFragmentManager
                .findFragmentById(R.id.mapView);
        MapsInitializer.initialize(getActivity());
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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }else{
                googleMap.setMyLocationEnabled(true);
                LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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

    //Response callback for "Location Update"
    private final JsonRequestManager.updateLocationRequest locationResponseCallback = new JsonRequestManager.updateLocationRequest() {
        @Override
        public void onSuccess(LocationResponseModel model) {
            if(progress!=null)
                progress.dismiss();
            if(model.getStatus().equalsIgnoreCase("success")){
                txtAddress.setText(model.getDetails().get(0)+"/"+model.getDetails().get(1));
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
    };



}
