package io.hussam.westarmy.onpullman.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import io.hussam.westarmy.onpullman.data.model.DayInfo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.StringRes;
import androidx.core.content.FileProvider;

public class StorageUtil {

    public static List<DayInfo> getPullmanPhotoDirectories(int pullmanId) {
        List<DayInfo> dirs = new ArrayList<>();
        File pullmanDaysDir = new File(getPullmanDaysDirectoryPath(pullmanId));
        if (pullmanDaysDir.isDirectory()) {
            for (File dayDir : pullmanDaysDir.listFiles()) {
                if (dayDir.isDirectory()) {
                    dirs.add(new DayInfo(dayDir.getName(), pullmanId));
                }
            }
        }

        return dirs;
    }

    private static String baseAppDirPath() {
        return Environment.getExternalStorageDirectory() + "/onpullman_app/";
    }

    private static String getPullmanDaysDirectoryPath(int pullmanId) {
        return baseAppDirPath() + "/" + pullmanId + "/";
    }

    private static String getPullmanSpecificDaysDirectoryPath(int pullmanId, String date) {
        return baseAppDirPath() + "/" + pullmanId + "/" + date + "/";
    }


    private static File getPullmanSpecificDayDirectory(int pullmanId, String date) {
        File dir = new File(getPullmanDaysDirectoryPath(pullmanId) + date + "/");
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File createImageFile(int pullmanId) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        File storageDir = getPullmanSpecificDayDirectory(pullmanId, timeStamp);
        File image = File.createTempFile(
                UUID.randomUUID().toString(),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static List<String> getAllPullmanDayPhotosPaths(DayInfo dayInfo) {
        List<String> paths = new ArrayList<>();
        File pullmanDaysDir = new File(getPullmanSpecificDaysDirectoryPath(dayInfo.getPullmanId(), dayInfo.getDate()));
        if (pullmanDaysDir.isDirectory()) {
            for (File photoPath : pullmanDaysDir.listFiles()) {
                if (photoPath.exists()) {
                    paths.add(photoPath.getAbsolutePath());
                }
            }
        }

        return paths;
    }

    public static Uri buildPhotoUri(Context context, @StringRes int intRes, File photoFile) {
        return FileProvider.getUriForFile(context,
                context.getString(intRes),
                photoFile);
    }

    public static boolean deleteFile(String photoPath) {
        File photoFile = new File(photoPath);
        if (photoFile.exists()) {
            return photoFile.delete();
        }
        return false;
    }
}
