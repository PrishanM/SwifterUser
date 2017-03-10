package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;

/**
 * Created by Prishan Maduka on 2/12/2017.
 */
public class PurchaseProductsCategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PurchaseItemsRecycleAdapter purchaseItemsRecycleAdapter;
    private ProgressDialog progress;
    private LayoutInflater inflate;


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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        purchaseItemsRecycleAdapter = new PurchaseItemsRecycleAdapter(AppController.getDatumArrayList(),getActivity());
        recyclerView.setAdapter(purchaseItemsRecycleAdapter);
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
