package cf.playhi.freezeyou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import java.io.File;

import cf.playhi.freezeyou.utils.ApplicationIconUtils;
import cf.playhi.freezeyou.utils.ApplicationInfoUtils;

import static cf.playhi.freezeyou.storage.key.DefaultSharedPreferenceStorageBooleanKeys.cacheApplicationsIcons;

public class PackageReplacedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            String pkgName = intent.getDataString();
            if (pkgName != null) {
                pkgName = pkgName.replace("package:", "");
                ApplicationInfo applicationInfo =
                        ApplicationInfoUtils.getApplicationInfoFromPkgName(pkgName, context);
                if (applicationInfo != null) {
                    // 检查设置并更新应用程序图标数据
                    if (cacheApplicationsIcons.getValue(context)) {
                        File file2 = new File(context.getCacheDir() + "/icon/" + pkgName + ".png");
                        if (file2.exists() && file2.isFile()) {
                            file2.delete();
                        }
                        File file = new File(context.getFilesDir() + "/icon/" + pkgName + ".png");
                        if (file.exists() && file.isFile()) {
                            file.delete();
                        }
                        ApplicationIconUtils
                                .getApplicationIcon(
                                        context, pkgName,
                                        applicationInfo, false, true);
                    }
                    //更新应用程序名称
                    context.getSharedPreferences("NameOfPackages", Context.MODE_PRIVATE)
                            .edit()
                            .putString(
                                    pkgName,
                                    context.getPackageManager()
                                            .getApplicationLabel(applicationInfo).toString())
                            .apply();
                }
            }
        }
    }
}
