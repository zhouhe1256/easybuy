package com.gaiya.easybuy.util;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by zhouh on 15-12-8.
 */
public class ClearWebview {

/**
 * 清除WebView缓存
 */
        public static void clearWebViewCache(Context context,String file ){

            //清理Webview缓存数据库
            try {
                context.deleteDatabase("webview.db");
                context.deleteDatabase("webviewCache.db");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //WebView 缓存文件
            File appCacheDir = new File(context.getFilesDir().getAbsolutePath()+file);

            File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()+"/"+file);

            //删除webview 缓存目录
            if(webviewCacheDir.exists()){
                deleteFile(webviewCacheDir);
            }
            //删除webview 缓存 缓存目录
            if(appCacheDir.exists()){
                deleteFile(appCacheDir);
            }

        }
    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e("file", "delete file no exists " + file.getAbsolutePath());
        }
    }

}
