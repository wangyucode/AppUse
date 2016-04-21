package cn.wycode.appuse;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 基类Activity
 * Created by wy on 2015/12/10.
 *
 * @author wy
 */
public abstract class BaseActivity extends AppCompatActivity {
    //当前的Context对象，方便内部类使用
    protected Context mContext;
    //布局填充器
    protected LayoutInflater mInflater;
    //标题栏
    protected Toolbar mToolBar;
    // 将被替换的内容布局
    protected FrameLayout mFmContent;
    // 标题返回局
    protected ImageView mIvTitleBack;
    // 标题名
    protected TextView mTvTitleName;
    // 标题右边文字
    protected TextView mTvTitleRight;
    // 标题右图片
    protected ImageView mIvTitleRight;
    //标题左文字
    protected TextView mTvTitleLeft;
    // 标题左图片
    protected ImageView mIvTitleLeft;
    //标题中间区域
    protected FrameLayout mFmTitleCenter;
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mContext = this;
        mInflater = getLayoutInflater();
        mHandler = new BaseHandler();
//        AppManager.getInstance().addActivity(this);// 将activity添加到list中
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EasyHttp.cancelRequest(this);
//        AppManager.getInstance().finishActivity(this);
        System.gc();
    }

    //返回事件
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * 初始化Views
     * 由基类Activity调用
     */
    protected abstract void initView();

    /**
     * 使用默认标题栏
     *
     * @param contentResID 内容布局id
     * @param titleName    标题文字
     */
    public void setContentViewWithDefaultTitle(@LayoutRes final int contentResID, @NonNull String titleName) {
        super.setContentView(R.layout.activity_base);
        mToolBar = getViewById(R.id.tb_base);
        mFmContent = getViewById(R.id.fm_base_content);
        mIvTitleLeft = getViewById(R.id.iv_title_left);
        mTvTitleLeft = getViewById(R.id.tv_title_left);
        mTvTitleRight = getViewById(R.id.tv_title_right);
        mIvTitleRight = getViewById(R.id.iv_title_right);
        mIvTitleBack = getViewById(R.id.iv_title_back);
        mTvTitleName = getViewById(R.id.tv_title);
        mFmTitleCenter = getViewById(R.id.fm_title_center);

        setSupportActionBar(mToolBar);
        if (contentResID > 0) {
            mInflater.inflate(contentResID, mFmContent);
        }
//        ButterKnife.bind(this);
        mTvTitleName.setText(titleName);
        setOnClickListeners(mOnClickListener, mIvTitleBack);
        initView();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
//        ButterKnife.bind(this);
        initView();
    }


    /**
     * 不需要为返回键设置监听，默认为finish当前activity
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_title_back: // 返回
                    onBackPressed();
                    break;
            }

        }
    };


    /**
     * 通过类名启动Activity
     *
     * @param cls 要跳转的Activity
     */
    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }




    /**
     * 查找View,不用强制转型
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return 对应的View
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 打印出当前类名的log，调用Log.d();
     *
     * @param s 需要打LOG的字符串
     */
    protected void logForDebug(String s) {
        if (!TextUtils.isEmpty(s))
            Log.d(this.getClass().getSimpleName(), s);
    }

    /**
     * 为多个View 添加点击事件
     *
     * @param listener
     * @param views
     */
    protected void setOnClickListeners(View.OnClickListener listener, View... views) {
        if (listener != null) {
            for (View view : views) {
                view.setOnClickListener(listener);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //友盟分析
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟分析
//        MobclickAgent.onPause(this);
    }

    /**
     * 设置标题中间区域的View
     *
     * @param view
     */
    protected void setTittleCenterView(View view) {
        mFmTitleCenter.removeAllViews();
        mFmTitleCenter.addView(view);
    }

    class BaseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            baseHandleMessage(msg);
        }
    }

    /**
     * 继承的mHandler使用的handleMessage方法 需要override
     *
     * @param msg 消息
     */
    protected void baseHandleMessage(Message msg) {

    }
}
