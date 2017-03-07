package com.evensel.swyftr.purchase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


/**
 * Created by Prishan Maduka on 8/9/2016.
 */
public class SearchItemsRecycleAdapter extends  RecyclerView.Adapter<SearchItemsRecycleAdapter.ImageViewHolder> {

    private final ArrayList<Integer> imageList;
    private ArrayList<String> names;
    private ArrayList<String> volumes;
    private ArrayList<String> prices;
    //private final LruCache<String, Bitmap> mLruCache;
    private Context context;

    public SearchItemsRecycleAdapter(ArrayList<Integer> imageList, ArrayList<String> names,ArrayList<String> volumes,ArrayList<String> prices, Context context){
        this.imageList = imageList;
        this.names = names;
        this.volumes = volumes;
        this.prices = prices;
        this.context = context;

        Log.d("xxxxxxxx",prices.size()+"");
        /*final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };*/
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_row, parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {

        holder.imgItem.setImageResource(imageList.get(position));
        holder.txtName.setText(names.get(position));
        holder.txtVolume.setText(volumes.get(position));
        holder.txtPrice.setText(prices.get(position));
        //holder.txtQuantity.setText(names.get(position));

        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent newActivity = new Intent(context, ImageViewerActivity.class);
                newActivity.putStringArrayListExtra("PATHS", imageList);
                newActivity.putExtra("POSITION", pos);
                context.startActivity(newActivity);*/
            }
        });

        final boolean[] favState = {false};
        final int[] qty = {0};
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favState[0]){
                    holder.imgFav.setImageResource(R.drawable.fav_check_unselected);
                    favState[0] =false;
                }else{
                    holder.imgFav.setImageResource(R.drawable.fav_check_selected);
                    favState[0] =true;
                }
            }
        });

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty[0] = qty[0] +1;
                holder.txtQuantity.setText("QTY "+qty[0]);

            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qty[0]!=0)
                    qty[0] = qty[0] -1;

                if(qty[0]<=0){
                    holder.txtQuantity.setText("QTY 0");
                }else{
                    holder.txtQuantity.setText("QTY "+qty[0]);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imgFav,imgPlus,imgMinus;
        public final CircularImageView imgItem;
        public final TextView txtName,txtVolume,txtPrice,txtQuantity;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imgFav = (ImageView)itemView.findViewById(R.id.imgFav);
            imgPlus = (ImageView)itemView.findViewById(R.id.imgPlus);
            imgMinus = (ImageView)itemView.findViewById(R.id.imgMinus);
            imgItem = (CircularImageView) itemView.findViewById(R.id.imgItem);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtVolume = (TextView) itemView.findViewById(R.id.txtVolume);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);
        }
    }

    /**
     *  This function will return the scaled version of original image.
     *  Loading original images into thumbnail is wastage of computation
     *  and hence we will take put scaled version.
     */
    private Bitmap getScaledImage (String imagePath){
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        bitmap = BitmapFactory.decodeFile(imagePath,options);

        return bitmap;
    }
}
