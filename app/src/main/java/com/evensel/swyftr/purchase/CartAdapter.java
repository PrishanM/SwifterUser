package com.evensel.swyftr.purchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.Datum;

/**
 * Created by Prishan Maduka on 10/7/2016.
 */
public class CartAdapter extends BaseAdapter {

    Context context;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
    private double total = 0.0;

    public CartAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return AppController.cartProducts.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = null;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(position!=AppController.cartProducts.size()){
                Datum datum = AppController.cartProducts.get(position);
                convertView = mInflater.inflate(R.layout.cart_item_list_row, null);
                final TextView txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
                final TextView txtUnitPrice = (TextView) convertView.findViewById(R.id.txtUnitPrice);
                final TextView txtWeight = (TextView) convertView.findViewById(R.id.txtWeight);
                final TextView txtQty = (TextView) convertView.findViewById(R.id.txtQty);

                txtProductName.setText(datum.getProductName());
                txtUnitPrice.setText("Unit Price : "+datum.getProductPrice());
                txtWeight.setText("Weight : "+datum.getProductAmount());
                txtQty.setText("Quantity : "+AppController.cartQuantity.get(datum.getId()));
                getTotal(datum.getProductPrice(),AppController.cartQuantity.get(datum.getId()));
            }
            else{
                convertView = mInflater.inflate(R.layout.cart_item_total_list_row, null);
                final TextView txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
                txtProductName.setText("Total : "+getTotal(0,0));
            }

        }

        return convertView;
    }

    private Double getTotal(int unitPrice,int units){

        total = total + (unitPrice*units);
        return total;
    }
}
