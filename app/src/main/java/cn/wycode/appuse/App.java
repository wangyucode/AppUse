package cn.wycode.appuse;

import android.graphics.drawable.Drawable;

/**
 *
 * Created by wangyu on 16/4/17.
 */
public class App {

    public Drawable drawable;
    public String name;
    public int count;
    public int time;

    public App(Drawable drawable, String name, int count, int time) {
        this.drawable = drawable;
        this.name = name;
        this.count = count;
        this.time = time;
    }
}
