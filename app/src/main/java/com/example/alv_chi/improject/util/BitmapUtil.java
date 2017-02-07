package com.example.alv_chi.improject.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by Alv_chi on 2017/1/15.
 */

public class BitmapUtil {

    public static Bitmap createCircleBitmap(Bitmap sourceBitmap, int theMinOfWidthAndHeightOfSourceBitmap) {
        if (sourceBitmap == null) {
            return null;
        }
        Paint paint = new Paint();//create paint
        paint.setAntiAlias(true);// againt the alias
//      create the targetBitmap
        Bitmap targetBitmap = Bitmap.createBitmap(theMinOfWidthAndHeightOfSourceBitmap, theMinOfWidthAndHeightOfSourceBitmap, Bitmap.Config.ARGB_8888);
//      put the targetBitmap as the canvas background
        Canvas canvas = new Canvas(targetBitmap);
//        draw a circle in the canvas
        canvas.drawCircle(theMinOfWidthAndHeightOfSourceBitmap / 2, theMinOfWidthAndHeightOfSourceBitmap / 2, theMinOfWidthAndHeightOfSourceBitmap / 2, paint);
//      set the mode that is the most important thing to do
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//      at last , draw the sourceBitmap to the circleCanvas
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);

        return targetBitmap;
    }



    public static Bitmap getScaledBitmap(Bitmap sourceBitmap,float destWidth,float destHeight)
    {
        if (sourceBitmap==null||destHeight==0||destWidth==0)
        {
            return null;
        }
//       get the sourceBitmap width and height
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        if (width==0||height==0)
        {
            return null;
        }

//        calculate the width scale and height scale
        float widthScale = destWidth / width;
        float heightScale = destHeight / height;

//        make a matrix to take the scale params
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale,heightScale);
//      create the scaleBitmap
        Bitmap scaledBitmap = Bitmap.createBitmap(sourceBitmap, 0,0, width, height, matrix, true);
        return scaledBitmap;
    }
}
