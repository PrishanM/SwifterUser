package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.evensel.swyftr.R;

import java.util.ArrayList;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class SearchProductItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PurchaseItemsRecycleAdapter purchaseItemsRecycleAdapter;

    private ArrayList<Integer> imageList;
    private ArrayList<String> descriptionList;
    private EditText edtSearch;

    public SearchProductItemsFragment() {
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

        edtSearch = (EditText)rootView.findViewById(R.id.txtSearch);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        purchaseItemsRecycleAdapter = new PurchaseItemsRecycleAdapter(imageList,descriptionList,getActivity());
        recyclerView.setAdapter(purchaseItemsRecycleAdapter);

        addFiles();

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
        return rootView;
    }

    private void performSearch() {
        FragmentManager fragmentManager = getParentFragment().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, new SearchProductsFragment());
        fragmentTransaction.commit();
        //getSupportActionBar().setTitle(title);
    }

    private void addFiles() {
        imageList.add(R.drawable.test_rice);
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
        descriptionList.add("Electronics");

        purchaseItemsRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
