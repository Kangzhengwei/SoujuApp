package souju.kzw.com.souju.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Android on 2019/3/14.
 */

public class PermissionUtil {

    public static boolean checkReadPermission(Activity mContext) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
            } else {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
            }
        }
        return false;
    }
}
