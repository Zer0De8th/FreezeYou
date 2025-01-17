package cf.playhi.freezeyou;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static cf.playhi.freezeyou.utils.FUFUtils.checkMRootFrozen;
import static cf.playhi.freezeyou.utils.FUFUtils.checkRootFrozen;

// Needs to be retained for compatibility
// with old FreezeYou structures and settings.
public class GetDisabledApplications extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<ApplicationInfo> applicationInfo = getApplicationContext().getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        int size = applicationInfo == null ? 0 : applicationInfo.size();
        String packageName;
        ArrayList<String> appList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            packageName = applicationInfo.get(i).packageName;
            if (checkRootFrozen(GetDisabledApplications.this, packageName, null) || checkMRootFrozen(GetDisabledApplications.this, packageName)) {
                appList.add(packageName);
            }
        }
        setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra("packages", appList));
        finish();
    }
}
