package com.evensel.swyftr.purchase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.evensel.swyftr.R;
import com.evensel.swyftr.deliveries.DeliveryOptionsActivity;
import com.evensel.swyftr.util.AppController;

/**
 * Created by Prishan Maduka on 3/18/2017.
 */
public class CartStepOneActivity extends AppCompatActivity {

    private SwipeMenuListView cartList;
    private CartAdapter mAdapter;
    private TextView toatlProducts;
    private Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Shoping Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cartList = (SwipeMenuListView)findViewById(R.id.listProducts);
        toatlProducts = (TextView)findViewById(R.id.txtTotalProductCount);
        toatlProducts.setText("You have "+ AppController.cartProducts.size()+" items in your shopping cart" );
        mAdapter = new CartAdapter(this);
        cartList.setAdapter(mAdapter);

        button = (Button)findViewById(R.id.btnProceed) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartStepOneActivity.this, DeliveryOptionsActivity.class);
                startActivity(intent);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(255, 0, 0)));
                // set item width
                deleteItem.setWidth(120);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);


                SwipeMenuItem favItem = new SwipeMenuItem(
                        getApplicationContext());
                favItem.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                favItem.setWidth(120);
                favItem.setIcon(R.drawable.fav_check_unselected);
                menu.addMenuItem(favItem);
            }
        };

        cartList.setMenuCreator(creator);
        cartList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        cartList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }
}
