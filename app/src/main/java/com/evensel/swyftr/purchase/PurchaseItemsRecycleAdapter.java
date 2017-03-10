package com.evensel.swyftr.purchase;

import android.content.Context;
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
import com.evensel.swyftr.util.Datum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Prishan Maduka on 8/9/2016.
 */
public class PurchaseItemsRecycleAdapter extends  RecyclerView.Adapter<PurchaseItemsRecycleAdapter.ImageViewHolder> {

    private final ArrayList<Datum> datumArrayList;
    private final LruCache<String, Bitmap> mLruCache;
    private Context context;

    public PurchaseItemsRecycleAdapter(ArrayList<Datum> dtum, Context context){
        this.datumArrayList = dtum;
        this.context = context;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_grid_row, parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Bitmap thumbnailImage = null;
        final String imageKey = datumArrayList.get(position).getImage();

        if(imageKey!=null && !imageKey.isEmpty()){
            thumbnailImage = getBitmapFromMemCache(imageKey);
        }

        final int pos = position;

        if (thumbnailImage == null){
            BitmapWorkerTask task = new BitmapWorkerTask(holder.imageView);
            task.execute(imageKey);
        }
        holder.imageView.setImageBitmap(thumbnailImage);
        holder.txtDescription.setText(datumArrayList.get(position).getCategoryName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent newActivity = new Intent(context, ImageViewerActivity.class);
                newActivity.putStringArrayListExtra("PATHS", imageList);
                newActivity.putExtra("POSITION", pos);
                context.startActivity(newActivity);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView txtDescription;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imgView);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
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
}
