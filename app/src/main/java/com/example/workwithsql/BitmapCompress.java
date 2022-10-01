package com.example.workwithsql;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapCompress {
    public static ByteArrayOutputStream BitmapCompressor(Bitmap b) {

        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        final int destWidth = origWidth/2;

        ByteArrayOutputStream outStream = null;
        if (origWidth > destWidth) {
            int destHeight = origHeight / (origWidth / destWidth);
            Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
            outStream = new ByteArrayOutputStream();
            b2.compress(Bitmap.CompressFormat.PNG, 20, outStream);
        }
        return outStream;
    }
}
