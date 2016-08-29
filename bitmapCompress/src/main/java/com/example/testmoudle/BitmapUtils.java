package com.example.testmoudle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * 创建一个可加载大图
 * Created by zhujiang on 16-8-26.
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    /**
     * 根据目标指定的寬高计算图片的sampleSize大小
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {

            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    || (halfWidth / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "reqWidth==" + reqWidth + ",reqHeight==" + reqHeight
                + ",inSampleSize==" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 根据给定的寬高计算出合适的图片
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeigth
     * @return
     */
    public static Bitmap decodeBitmapSampleBitmapFromResource(Resources res
            , int resId, int reqWidth, int reqHeigth) {

        // Get image width height and mimeType,but bitmap object is null.
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeigth);

        // Decode bitmap with inSampleSize in
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 异步加载图片的bitmap drawable
     */
    static class AsyncBitmapDrawable extends BitmapDrawable {

        private WeakReference<BitmapWorkTask> bitmapWorkWeakReference;


        public AsyncBitmapDrawable(Resources res, Bitmap bitmap
                , BitmapWorkTask task) {
            super(res, bitmap);
            bitmapWorkWeakReference = new WeakReference<BitmapWorkTask>(task);
        }

        public BitmapWorkTask getBitmapWorkTask() {
            return bitmapWorkWeakReference.get();
        }


    }

    /**
     * 取消潜在的工作线程
     *
     * @param data
     * @param imageView
     * @return
     */
    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkTask mTask = getBitmapWorkTask(imageView);
        if (mTask != null) {
            final int bitmapData = mTask.data;
            if (bitmapData == 0 || bitmapData != data) {
                mTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取一个bitmap work task
     *
     * @param imageView
     * @return
     */
    private static BitmapWorkTask getBitmapWorkTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncBitmapDrawable) {
                final AsyncBitmapDrawable asyncBitmapDrawable = (AsyncBitmapDrawable) drawable;
                return asyncBitmapDrawable.getBitmapWorkTask();
            }
        }
        return null;
    }


    /**
     * 使用一个AsyncTask来加载大图数据
     */
    static class BitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {

        // 使用一个弱引用来保存imageView的实例
        private WeakReference<ImageView> mImageWeakReference;
        public int data = 0;
        private Context ctx;
        private int mWidth;
        private int mHeight;
        // 图片缓存
        private ImageCache mMemoryCache;


        public BitmapWorkTask(ImageView imageView, Context context
                , int width, int height) {
            mImageWeakReference = new WeakReference<ImageView>(imageView);
            this.ctx = context;
            this.mWidth = width;
            this.mHeight = height;
        }

        public BitmapWorkTask(ImageCache imageCache, ImageView imageView, Context context
                , int width, int height) {
            mImageWeakReference = new WeakReference<ImageView>(imageView);
            this.ctx = context;
            this.mWidth = width;
            this.mHeight = height;
            this.mMemoryCache = imageCache;
        }


        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            Bitmap bm = BitmapUtils.decodeBitmapSampleBitmapFromResource(ctx.getResources()
                    , data, mWidth, mHeight);
            // 添加到缓存中
            if (mMemoryCache != null) {
                Log.d(TAG, "data===" + data);
                mMemoryCache.addBitmapToMemoryCache(String.valueOf(data), bm);
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (mImageWeakReference != null && bitmap != null) {
                ImageView imageView = mImageWeakReference.get();
//                final BitmapWorkTask mTask = getBitmapWorkTask(imageView);
//                if (this == mTask && imageView != null) {
//                    imageView.setImageBitmap(bitmap);
//                }
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }


            }
        }
    }


    /**
     * 获取应用程序最大内存
     *
     * @return
     */
    private static int getMaxMemory() {
        return (int) (Runtime.getRuntime().maxMemory() / 1024);
    }

    /**
     * 将应用程序最大内存的1/8分配给缓存大小
     *
     * @return
     */
    public static int getCacheSize() {
        return getMaxMemory() / 8;
    }


}
