package com.evensel.swyftr.purchase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evensel.swyftr.R;
import com.evensel.swyftr.util.AppController;
import com.evensel.swyftr.util.AppURL;
import com.evensel.swyftr.util.Constants;
import com.evensel.swyftr.util.Datum;
import com.evensel.swyftr.util.JsonRequestManager;
import com.evensel.swyftr.util.ResponseModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Prishan Maduka on 8/9/2016.
 */
public class SearchItemsRecycleAdapter extends  RecyclerView.Adapter<SearchItemsRecycleAdapter.ImageViewHolder> {

    private ArrayList<Datum> datumArrayList;
    private Context context;
    private ProgressDialog progress;
    private String token;
    private Listener listener;
    private final LruCache<String, Bitmap> mLruCache;

    public SearchItemsRecycleAdapter(ArrayList<Datum> datumArrayList, Context context,Listener listener ){
        this.datumArrayList = datumArrayList;
        this.context = context;
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.LOGIN_SHARED_PREF, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.LOGIN_ACCESS_TOKEN, "");
        this.listener = listener;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_row, parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        Bitmap thumbnailImage = null;
        final String imageKey = datumArrayList.get(position).getImage();

        if(imageKey!=null && !imageKey.isEmpty()){
            thumbnailImage = getBitmapFromMemCache(imageKey);
        }



        holder.txtName.setText(datumArrayList.get(position).getProductName());
        holder.txtVolume.setText(datumArrayList.get(position).getProductAmount()+"");
        holder.txtPrice.setText(datumArrayList.get(position).getProductPrice()+"");

        if (thumbnailImage == null){
            BitmapWorkerTask task = new BitmapWorkerTask(holder.imgItem);
            task.execute(imageKey);
        }
        holder.imgItem.setImageBitmap(thumbnailImage);

        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final boolean[] favState = {false};
        final int[] qty = {0};

        if(datumArrayList.get(position).getFavourite()!=null && datumArrayList.get(position).getFavourite().equals("1")){
            holder.imgFav.setImageResource(R.drawable.fav_check_selected);
            favState[0] = true;
        }else{
            holder.imgFav.setImageResource(R.drawable.fav_check_unselected);
            favState[0] = false;
        }

        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favState[0]){
                    holder.imgFav.setImageResource(R.drawable.fav_check_unselected);
                    favState[0] =false;
                    favouriteProduct(datumArrayList.get(position).getId(),0);
                    datumArrayList.get(position).setFavourite("0");
                }else{
                    holder.imgFav.setImageResource(R.drawable.fav_check_selected);
                    favState[0] =true;
                    favouriteProduct(datumArrayList.get(position).getId(),1);
                    datumArrayList.get(position).setFavourite("1");
                }
            }
        });

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty[0] = qty[0] +1;
                holder.txtQuantity.setText("QTY "+qty[0]);
                AppController.setAmount(AppController.getAmount()+datumArrayList.get(position).getProductPrice());
                if (listener != null) {
                    listener.onOptionsMenuChangeRequested();
                }

            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qty[0]>0){
                    qty[0] = qty[0] -1;
                    AppController.setAmount(AppController.getAmount()-datumArrayList.get(position).getProductPrice());
                    holder.txtQuantity.setText("QTY "+qty[0]);
                }else{
                    holder.txtQuantity.setText("QTY 0");
                }
                if (listener != null) {
                    listener.onOptionsMenuChangeRequested();
                }


            }
        });
    }

    private void favouriteProduct(int productId,int status) {

        progress = ProgressDialog.show(context, null,
                "Loading...", true);
        JsonRequestManager.getInstance(context).favouriteProduct(AppURL.APPLICATION_BASE_URL+AppURL.PRODUCT_FAVOURITE,token,productId,status, favRequest);
    }

    //Response callback for "Get category List"
    private final JsonRequestManager.favouriteProductRequest favRequest = new JsonRequestManager.favouriteProductRequest() {
        @Override
        public void onSuccess(ResponseModel model) {

            if(progress!=null)
                progress.dismiss();
            AppController.setDatumArrayList(datumArrayList);
            notifyDataSetChanged();
        }

        @Override
        public void onError(String status) {
            if(progress!=null)
                progress.dismiss();
        }

        @Override
        public void onError(ResponseModel model) {
            if(progress!=null)
                progress.dismiss();
        }
    };

    @Override
    public int getItemCount() {
        return datumArrayList.size();
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

    private Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            final Bitmap bitmap = getScaledImage(params[0]);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        //  onPostExecute() sets the bitmap fetched by doInBackground();
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && key!=null && bitmap!=null) {
            mLruCache.put(key, bitmap);
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

    public interface Listener {
        void onOptionsMenuChangeRequested();
    }


}
