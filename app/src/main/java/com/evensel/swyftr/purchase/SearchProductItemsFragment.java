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
import com.evensel.swyftr.util.AppController;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class SearchProductItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchItemsRecycleAdapter searchItemsRecycleAdapter;
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

        category = (TextView)rootView.findViewById(R.id.txtCat);
        category.setVisibility(View.GONE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        searchItemsRecycleAdapter = new SearchItemsRecycleAdapter(AppController.getSearchArrayList(),getActivity());
        recyclerView.setAdapter(searchItemsRecycleAdapter);
        return rootView;
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
