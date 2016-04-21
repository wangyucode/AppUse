package cn.wycode.appuse;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ListView listView;

    View dialogView;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithDefaultTitle(R.layout.activity_main, "使用统计");


        startService(new Intent(this, AppService.class));


    }

    @Override
    protected void initView() {
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setText("设置");

        listView = (ListView) findViewById(R.id.lv_main);

        dialogView = getLayoutInflater().inflate(R.layout.dialog_setting, null);

        dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setNegativeButton("取消", null)
                .create();


        ToggleButton button = (ToggleButton) dialogView.findViewById(R.id.tb_dialog);
        LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.ll_clean);

        button.setOnCheckedChangeListener(this);

        setOnClickListeners(this, mTvTitleRight, linearLayout);

        getData();


    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        List<PackageInfo> infoList = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);

        List<App> apps = new ArrayList<>();

        PreferencesManager preferencesManager = PreferencesManager.getInstance(this);

        for (PackageInfo info : infoList) {
//            if (!info.packageName.startsWith("")) {

            Drawable icon = info.applicationInfo.loadIcon(getPackageManager());
            String name = info.applicationInfo.loadLabel(getPackageManager()).toString();
            int count = preferencesManager.get(info.packageName + "_COUNT", 0);
            int time = preferencesManager.get(info.packageName + "_TIME", 0);

            apps.add(new App(icon, name, count, time));
//            }
        }

        MainAdapter adapter = new MainAdapter(this, apps);

        listView.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_title_right) {
            if (!dialog.isShowing())
                dialog.show();
        } else {
            for (PackageInfo info : getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES)) {
                PreferencesManager.getInstance(this).put(info.packageName + "_TIME", 0);
                PreferencesManager.getInstance(this).put(info.packageName + "_COUNT", 0);
            }

            dialog.dismiss();
            getData();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PreferencesManager.getInstance(this).put("isChecked", isChecked);
    }
}
