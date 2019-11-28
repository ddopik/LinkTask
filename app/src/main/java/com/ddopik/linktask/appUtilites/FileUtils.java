package com.ddopik.linktask.appUtilites;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.*;


public class FileUtils {
    private static final String APP_NAME = "/" + "PhLog";
    public static final String MAIN_DIRECTORY = Environment.getExternalStorageDirectory() + APP_NAME;

    public static final String DOWNLOADS = MAIN_DIRECTORY + "/.Downloads";
    public static final String TEMP_FILES = MAIN_DIRECTORY + "/.temp_files";
    public static final String AVATAR_DIRECTORY = MAIN_DIRECTORY + "/.Avatar";

    /**
     * @param directory          Where to save your file
     * @param fileName           Image Name to be saved
     * @param bitmap             Image to be saved with
     * @param compressionQuality Compression Ratio to be used when compressing bitmap
     * @return Absolute File Path if the Bitmap was compressed and written
     * Or Empty String in case of failure
     * @throws IOException
     */
    private String writeBitmapToFile(File directory, String fileName, Bitmap bitmap, int compressionQuality) throws IOException {
        File file = new File(directory, fileName);
        if (!new DiskHelper().isSdCardMounted()) return "";

//        if (!file.isDirectory()) file.mkdirs();
        if (file.exists())
            file.delete();
        if (directory.mkdirs() || directory.isDirectory()) {
            FileOutputStream outputStream = new FileOutputStream(file);
            boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, outputStream);
            Log.i(getClass().getSimpleName(), file.getAbsolutePath() + " was compressed: " + b);

            outputStream.flush();
            outputStream.close();
            return file.getAbsolutePath();
        }
        return "";
    }

    public String writeImageToFile(String directory, String fileName, Bitmap bitmap) throws IOException {
        File directoryFolder = new File(directory);
        String filePath = writeBitmapToFile(directoryFolder, fileName, bitmap, 90);
        Log.i(getClass().getSimpleName(), "File Path: " + filePath);
        return filePath;
    }


    public boolean writeImageToFile(String directory, String fileName, Bitmap bitmap, int compressionRatio) throws IOException {
        File directoryFolder = new File(directory);
        return writeBitmapToFile(directoryFolder, fileName, bitmap, compressionRatio).contains(fileName);
    }


    //Copied From Common class from Version 1
    public void addSavedImageToGallery(String fileName, Context context) {
        File file = new File("file:" + fileName);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        }
    }

    //SRC: http://developer.android.com/training/camera/photobasics.html
    public void setBitmapToImage(String mCurrentPhotoPath, ImageView mImageView) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = 0;
        // Determine how much to scale down the image
        if (targetW != 0 && targetH != 0)
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        else scaleFactor = 0;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        mImageView.setImageBitmap(bitmap);
    }

    public String getPathFromUri(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    public static void deleteFilesInDirectory(File file) {
        if (file.isDirectory())
            for (File child : file.listFiles())
                deleteFilesInDirectory(child);

        file.delete();
    }

    public static void deleteFilesWithDirectory(File file) {
        if (file.isDirectory())
            for (File child : file.listFiles())
                deleteFilesInDirectory(child);

        if (!file.isDirectory())
            file.delete();
    }

    public static String getImgName(String imgPath) {
        return imgPath.substring(imgPath.lastIndexOf("/") + 1);
    }

    static public void copyFile(File src, File dst) throws IOException {
        InputStream inuptStream = new FileInputStream(src);
        OutputStream outputStream = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inuptStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inuptStream.close();
        outputStream.close();
    }

    public void deleteFiles(String directoryName) {
        File directory = new File(directoryName);
        deleteFile(directory);
    }

    public void deleteFile(File file) {
        if (file.isDirectory())
            for (File child : file.listFiles())
                deleteFile(child);
        file.delete();
    }
    public static void moveFile(File src, File dst) throws IOException {
        InputStream inuptStream = new FileInputStream(src);
        OutputStream outputStream = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inuptStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inuptStream.close();
        outputStream.close();

        src.delete();
    }

    public static void renameFile(File directory, String oldName, String newName) throws IOException {
        File oldFile = new File(directory,oldName);
        File newFile = new File(directory,newName);
        oldFile.renameTo(newFile);
    }


}
