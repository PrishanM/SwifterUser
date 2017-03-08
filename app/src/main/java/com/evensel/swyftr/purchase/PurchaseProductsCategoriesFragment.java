package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.CategoriesResponse;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.Datum;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.Notifications;

import java.util.ArrayList;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class PurchaseProductsCategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PurchaseItemsRecycleAdapter purchaseItemsRecycleAdapter;
    private ProgressDialog progress;
    private LayoutInflater inflate;
    private View layout;
    private String token = "";
    private final Handler handler = new Handler();
    private Runnable runnable;

    ArrayList<Datum> datumArrayList = new ArrayList<>();

    private ArrayList<Integer> imageList;
    private ArrayList<String> descriptionList;


    public PurchaseProductsCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_purchase, container, false);

        imageList = new ArrayList<>();
        descriptionList = new ArrayList<>();

        inflate = inflater;
        layout = inflate.inflate(R.layout.custom_toast_layout,(ViewGroup) rootView.findViewById(R.id.toast_layout_root));
        SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.LOGIN_ACCESS_TOKEN, "");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        purchaseItemsRecycleAdapter = new PurchaseItemsRecycleAdapter(datumArrayList,getActivity());
        recyclerView.setAdapter(purchaseItemsRecycleAdapter);

        addFiles();
        return rootView;
    }

    private void addFiles() {
        /*imageList.add(R.drawable.test_rice);
        imageList.add(R.drawable.test_sauce);
        imageList.add(R.drawable.test_biscuits);
        imageList.add(R.drawable.test_electronics);
        imageList.add(R.drawable.test_biscuits);
        imageList.add(R.drawable.test_electronics);

        descriptionList.add("Rice");
        descriptionList.add("Sauce");
        descriptionList.add("Biscuits");
        descriptionList.add("Electronics");
        descriptionList.add("Biscuits");
        descriptionList.add("Electronics");*/

        progress = ProgressDialog.show(getActivity(), null,
                "Loading...", true);
        JsonRequestManager.getInstance(getActivity()).getCategories(AppURL.APPLICATION_BASE_URL+AppURL.GET_CATEGORY_LIST+"?page=1",token, getCategoriesRequest);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Response callback for "User Login"
    private final JsonRequestManager.getCategoriesRequest getCategoriesRequest = new JsonRequestManager.getCategoriesRequest() {
        @Override
        public void onSuccess(CategoriesResponse model) {


            if(model.getStatus().equalsIgnoreCase("success") && model.getDetails().getNextPageUrl()!=null){
                for(Datum datum :model.getDetails().getData()){
                    datumArrayList.add(datum);
                }
                getNextList(model.getDetails().getNextPageUrl());
            }else{
                if(progress!=null)
                    progress.dismiss();
                Notifications.showToastMessage(layout,getActivity(),model.getMessage()).show();
                purchaseItemsRecycleAdapter.notifyDataSetChanged();

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

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
