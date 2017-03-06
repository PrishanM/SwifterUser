package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.os.Bundle;
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
        setupViewPager();

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        imgHome.setOnClickListener(this);


        return rootView;
    }

    private void performSearch() {
        viewPager.getCurrentItem();
        viewPager.setAdapter(null);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Foodcity");
        titles.add("Grocerymart");
        searchItemPagerAdapter =
                new SearchItemPagerAdapter(getChildFragmentManager(),titles);
        viewPager.setAdapter(searchItemPagerAdapter);
        imgHome.setVisibility(View.VISIBLE);

    }

    private void setupViewPager() {

        ArrayList<String> titles = new ArrayList<>();
        titles.add("Foodcity");
        titles.add("Grocerymart");
        titles.add("Laugfs");

        purchaseItemPagerAdapter =
                new PurchaseItemPagerAdapter(getChildFragmentManager(),titles);
        viewPager.setAdapter(purchaseItemPagerAdapter);
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
}
