package cn.wycode.appuse;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangyu on 16/4/16.
 */
public class MainAdapter extends BaseAdapter {

    Context mContext;
    List<App> list;

    public MainAdapter(Context context, List<App> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main, null);
        }
        App app = list.get(position);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_item_main);
        tvName.setText(app.name);

        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_main);
        ivIcon.setImageDrawable(app.drawable);

        TextView tvCount = (TextView) convertView.findViewById(R.id.tv_number);
        tvCount.setText(mContext.getString(R.string.count, app.count));


        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        if (app.time > 3600) {
            int hour = app.time / 3600;
            int minute = (app.time % 3600) / 60;
            int seconds = app.time % 60;
            tvTime.setText(mContext.getString(R.string.time_hour, hour, minute, seconds));

        } else if (app.time > 60) {
            int minute = app.time / 60;
            int seconds = app.time % 60;
            tvTime.setText(mContext.getString(R.string.time_minute, minute, seconds));
        } else {
            tvTime.setText(mContext.getString(R.string.time, app.time));
        }


        return convertView;
    }
}
