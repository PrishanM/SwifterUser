package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evensel.swyftr.R;

import java.util.ArrayList;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class SearchProductItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchItemsRecycleAdapter searchItemsRecycleAdapter;

    private ArrayList<Integer> imageList;
    private ArrayList<String> names;
    private ArrayList<String> volumes;
    private ArrayList<String> prices;
    private TextView category;

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
        prices = new ArrayList<>();
        names = new ArrayList<>();
        volumes = new ArrayList<>();

        category = (TextView)rootView.findViewById(R.id.txtCat);
        category.setVisibility(View.GONE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        searchItemsRecycleAdapter = new SearchItemsRecycleAdapter(imageList,names,volumes,prices,getActivity());
        recyclerView.setAdapter(searchItemsRecycleAdapter);

        addFiles();
        return rootView;
    }

    private void addFiles() {
        imageList.add(R.drawable.test_rice);
        imageList.add(R.drawable.test_sauce);
        imageList.add(R.drawable.test_biscuits);

        names.add("Kist Chili Sauce");
        names.add("Kist Tomato sauce");
        names.add("Netro Chili Sauce");

        volumes.add("350ML");
        volumes.add("350ML");
        volumes.add("250ML");

        prices.add("LKR 500");
        prices.add("LKR 300");
        prices.add("LKR 500");

        imageList.add(R.drawable.test_rice);
        imageList.add(R.drawable.test_sauce);
        imageList.add(R.drawable.test_biscuits);

        names.add("Kist Chili Sauce");
        names.add("Kist Tomato sauce");
        names.add("Netro Chili Sauce");

        volumes.add("350ML");
        volumes.add("350ML");
        volumes.add("250ML");

        prices.add("LKR 500");
        prices.add("LKR 300");
        prices.add("LKR 500");

        searchItemsRecycleAdapter.notifyDataSetChanged();
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
