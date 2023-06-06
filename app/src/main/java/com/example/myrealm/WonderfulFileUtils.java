package com.example.myrealm;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class WonderfulFileUtils {
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable();
    }

    public static File getFileFromUrl(String url) {

        File file = null;
        FileOutputStream fileOutputStream;
        try {
            byte[] bytes = url.getBytes();
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ATC/" + "code.jpg");
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes, 0, bytes.length);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File getLongSaveFile(Context context, String dirname, String filename) {
        File file;
        if (hasSDCard()) {
            file = new File(context.getExternalFilesDir(dirname), filename);
        } else {
            file = new File(context.getFilesDir(), filename);
        }
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(System.currentTimeMillis());
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
        if (!storageDir.exists())
            storageDir.mkdirs();
        File image = File.createTempFile(
                timeStamp,                   /* prefix */
                ".jpeg",                     /* suffix */
                storageDir                   /* directory */
        );
        return image;
    }

    public static File getLongSaveDir(Context context, String dirname) throws IOException {
        File file;
        if (hasSDCard()) {
            file = context.getExternalFilesDir(dirname);
        } else {
            file = context.getFilesDir();
        }
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static File getCacheSaveFile(Context context, String filename) {
        File file;
        String path = "root";
        if (hasSDCard()) {
            file = new File(context.getExternalCacheDir(), filename);
        } else {
            file = new File(context.getCacheDir(), filename);
        }

        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static File getCacheSaveDir(Context context) throws IOException {
        File file;
        if (hasSDCard()) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static File getCommonPathFile(Context context, String filename) {
        File file = new File(Environment.getExternalStorageDirectory() + filename);
        if (file.isDirectory())
            return null;
        if (file.exists())
            file.delete();
        if (!file.getParentFile().exists())
            file.mkdirs();
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getUriForFile(Context context, File file) {
        return Build.VERSION.SDK_INT >= 24 ? getUriForFile24(context, file) : Uri.fromFile(file);
    }

    public static Uri getUriForFile24(Context context, File file) {
        //return FileProvider.getUriForFile(context, "com.zooex.app.fileprovider", file);
        String authority = BuildConfig.APPLICATION_ID;
        return FileProvider.getUriForFile(context, authority + ".fileprovider", file);
    }

    public static String getMimeType(File file) {
        String type = null;
        final String url = file.toString();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if (type == null) {
            type = "image/*"; // fallback type. You might set it to */*
        }
        return type;
    }
}
