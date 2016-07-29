package com.windward.sharelibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ww on 2016/1/20.
 */
public class BitmapUtils {

    public static byte[] bmpToByteArray(Bitmap bmp, final boolean needRecycle) {
        bmp = comp(bmp);
        byte[] result = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
            if (needRecycle) {
                bmp.recycle();
            }
            result = output.toByteArray();
            Log.w("bti", "img size  " + (result.length / 1024));
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        if (bytes.length / 1024 < 30) {
            return BitmapFactory.decodeStream(isBm);
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inSampleSize = computeSampleSize(newOpts, -1, 99 * 99);
        newOpts.inJustDecodeBounds = false;
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static String saveTempBitmap(Context mContext, Bitmap bitmap) {
        if (bitmap != null
                && Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File imgFile = null;
            File tempDir = Environment.getExternalStorageDirectory();
            String path = tempDir.getAbsolutePath() + File.separator
                    + "feno_cache" + File.separator;
            File dir = new File(path);
            doMkdir(dir);
            try {
                String fileName = String.valueOf(System.currentTimeMillis());
                imgFile = new File(dir, fileName + ".jpg");
                FileOutputStream fos = new FileOutputStream(imgFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return imgFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean doMkdir(File dirFile) {
        try {
            boolean bFile = dirFile.exists();
            if (bFile == true) {
                return true;
            } else {
                bFile = dirFile.mkdirs();
                if (bFile == true) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }


}
