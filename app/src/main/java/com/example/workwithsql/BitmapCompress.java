package com.example.workwithsql;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapCompress {
    public static ByteArrayOutputStream BitmapCompressor(Bitmap b) throws IOException {

        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        final int destWidth = 500;//or the width you need

        ByteArrayOutputStream outStream = null;
        if (origWidth > destWidth) {
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight / (origWidth / destWidth);
            // we create an scaled bitmap so it reduces the image, not just trim it
            Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
            outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.PNG, 50, outStream);

        }
        return outStream;
    }
}
