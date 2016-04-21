package cn.wycode.appuse;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;

/**
 * Created by wangyu on 16/4/17.
 */
public class AppService extends Service {

    private static final String TAG = "AppService";

    private ActivityManager.RunningAppProcessInfo currentProcess;

    private String lastTask, currentTask;

    DAO dao;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "started");
        final ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(AppService.this, "仅支持Android5.0以下机型获取用量信息!", Toast.LENGTH_LONG).show();
            this.stopSelf();
            return;
        }


        new Thread() {
            public void run() {
                while (true) {

                    Calendar calendar = Calendar.getInstance();


                    if (calendar.get(Calendar.DATE) != PreferencesManager.getInstance(AppService.this).get("date", 0)) {

                        PreferencesManager.getInstance(AppService.this).put("date", calendar.get(Calendar.DATE));

                        if (PreferencesManager.getInstance(AppService.this).get("isChecked", false)) {
                            for (PackageInfo info : getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES)) {
                                PreferencesManager.getInstance(AppService.this).put(info.packageName+ "_TIME",0);
                                PreferencesManager.getInstance(AppService.this).put(info.packageName+ "_COUNT",0);
                            }
                        }
                    }


                    List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(10);

                    currentTask = tasks.get(0).topActivity.getPackageName();


                    if (lastTask != null && lastTask.equals(currentTask)) {
                        Log.d(TAG, lastTask + "=>Running");
                        int time = PreferencesManager.getInstance(AppService.this).get(lastTask + "_TIME", 0);
                        time++;
                        PreferencesManager.getInstance(AppService.this).put(lastTask + "_TIME", time);

                    } else {
                        Log.d(TAG, currentTask + "=>Start");

                        int count = PreferencesManager.getInstance(AppService.this).get(currentTask + "_COUNT", 0);

                        count++;

                        PreferencesManager.getInstance(AppService.this).put(currentTask + "_COUNT", count);

                    }

                    lastTask = currentTask;


                    try {


                        sleep(950);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getTopActivityFromLollipopOnwards() {
        String topPackageName = "";
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds

        Map<String, UsageStats> map = mUsageStatsManager.queryAndAggregateUsageStats(time - 1000 * 10, time);
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                Log.e("TopPackage Name", topPackageName);
            }
        }
        return topPackageName;
    }
}
