package com.example.testmoudle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * 写测试指定一个寬高的图片
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv1 = (ImageView) findViewById(R.id.image1);
        ImageView iv2 = (ImageView) findViewById(R.id.image2);

        Bitmap bm1 = BitmapUtils.decodeBitmapSampleBitmapFromResource(getResources()
                , R.drawable.beautiful, 100, 100);
//        Bitmap bm2 = BitmapUtils.decodeBitmapSampleBitmapFromResource(getResources()
//                , R.drawable.sence, 100, 100);
//
//        iv1.setImageBitmap(bm1);
//        iv2.setImageBitmap(bm2);

//        Log.i(TAG, "bm1 width==" + bm1.getWidth() + ",height===" + bm1.getHeight());
//        Log.i(TAG, "bm2 width==" + bm2.getWidth() + ",height===" + bm2.getHeight());


//        new LoadLargeImageTask(iv1).execute(R.drawable.beautiful);
//        new LoadLargeImageTask(iv2).execute(R.drawable.sence);


//        bm1 = mergeImage(bm1);
//        iv1.setImageBitmap(bm1);
        loadBitmap(R.drawable.beautiful, iv1);
        loadBitmap(R.drawable.sence, iv2);

    }


    public void loadBitmap(int resId, ImageView imageView) {
        if (BitmapUtils.cancelPotentialWork(resId, imageView)) {
            final BitmapUtils.BitmapWorkTask mTask =
                    new BitmapUtils.BitmapWorkTask(imageView, this,100,100);
            Bitmap bm1 = BitmapUtils.decodeBitmapSampleBitmapFromResource(getResources()
                    , R.drawable.beautiful, 100, 100);
            final BitmapUtils.AsyncBitmapDrawable asyncBitmapDrawable =
                    new BitmapUtils.AsyncBitmapDrawable(getResources(), bm1, mTask);
            imageView.setImageDrawable(asyncBitmapDrawable);
            mTask.execute(resId);
        }
    }


    /**
     * 将两张图片合并为一张图片
     *
     * @param bitmap
     * @return
     */
    private Bitmap mergeImage(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        int newWidth = bitmap.getWidth() * 2;
        int newHeight = bitmap.getHeight() * 2;
        Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        Paint p = new Paint();
        canvas.drawBitmap(bitmap, 0, 0, p);
        canvas.drawBitmap(bitmap, 0, bitmap.getHeight(), p);
        return newBitmap;
    }


}
