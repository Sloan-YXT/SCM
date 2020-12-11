package com.team7.app.common;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HttpPostUploadUtil {
    public interface CallBack
    {
        public void loadData(Message msg);
    }
    public static void formUpload(final String urlStr,final String path,final byte[] content,final CallBack callBack) {
        final String BOUNDARY = "---------------------------123821742118716";
        final android.os.Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                callBack.loadData(msg);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = null;
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(30000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("GET");
                    //only a suggestion , do not dependOn it!
                    conn.setRequestProperty("Connection", "Keep-Alive");
        /*            conn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + BOUNDARY);

         */
                    Log.e("path","line-50");
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                    OutputStream fout = conn.getOutputStream();
                    out.write(path.getBytes());
                    out.write('\r');
                    out.write('\n');
                    Log.e("length","len:::::::"+content.length);

                    out.writeInt(content.length);
                    out.write('\r');
                    out.write('\n');

                    out.write(content);
                    out.close();
                    conn.connect();
                    Log.e("length",new String(content));
                    handler.sendEmptyMessage(conn.getResponseCode());
                } catch (Exception e) {
                    System.out.println("发送POST请求出错。" + urlStr);
                    Log.e("path",Log.getStackTraceString(e));
                }
            }
        }).start();

    }

}
