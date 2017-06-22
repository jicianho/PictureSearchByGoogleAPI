package com.takepickpicturedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 10411024 on 2016/3/11.
 */
public class DownloadWebPicture {

    public synchronized Bitmap getUrlPic(String url) {

        Bitmap webImg = null;

        try {
            URL imgUrl = new URL(url);
            HttpURLConnection httpURLConnection
                    = (HttpURLConnection) imgUrl.openConnection();
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            int length = (int) httpURLConnection.getContentLength();
            int tmpLength = 512;
            int readLen = 0,desPos = 0;
            byte[] img = new byte[length];
            byte[] tmp = new byte[tmpLength];
            if (length != -1) {
                while ((readLen = inputStream.read(tmp)) > 0) {
                    System.arraycopy(tmp, 0, img, desPos, readLen);
                    desPos += readLen;
                }
                webImg = BitmapFactory.decodeByteArray(img, 0, img.length);
                if(desPos != length){
                    throw new IOException("Only read" + desPos +"bytes");
                }
            }
            httpURLConnection.disconnect();
        }
        catch (IOException e) {
            Log.e("IOException", e.toString());
        }
        return webImg;
    }

}
